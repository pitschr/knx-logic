/*
 * Copyright (C) 2021 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.logic.db.loader;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.exceptions.LoaderException;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.StaticPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Mapper for {@link Component} to read from database
 * <p>
 * This class will reads following database tables:
 * <ul>
 * <li>components</li>
 * <li>connectors</li>
 * <li>pins</li>
 * </ul>
 *
 * @author PITSCHR
 */
abstract class AbstractComponentLoader<T extends Component> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentLoader.class);
    protected final DatabaseManager databaseManager;

    protected AbstractComponentLoader(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    /**
     * Loads the {@link Component} which is an instance of {@code T} from the
     * database using the given primary key {@code id}
     *
     * @param id the primary key
     * @return the loaded component
     */
    public abstract T loadById(final int id);

    /**
     * Maps the {@link Pin} / {@link Connector} from class with {@link ConnectorModel} from database.
     *
     * @param connectors      list of connectors; may not be null
     * @param connectorModels list of {@link ConnectorModel}; they must be already filtered and may not be null
     */
    protected void updateConnectors(final List<? extends Connector> connectors, final List<ConnectorModel> connectorModels) {
        if (connectors.size() != connectorModels.size()) {
            throw new LoaderException(String.format("Size of input connector doesn't match (class: %s, database: %s)", connectors.size(),
                    connectorModels.size()));
        }

        for (final var connector : connectors) {
            final var fieldName = connector.getDescriptor().getName();

            final var connectorModel = connectorModels.stream().filter(c -> fieldName.equals(c.getConnectorName())).findFirst()
                    .orElseThrow(() -> new LoaderException(
                            String.format("No connector model match found for '%s' in: %s",
                                    fieldName,
                                    connectorModels.stream().map(ConnectorModel::getConnectorName).collect(Collectors.toList())
                            )
                    ));

            // check data integrity: static or dynamic?
            if (connectorModel.getBindingType() == BindingType.STATIC && connector instanceof StaticConnector) {
                final StaticConnector staticFieldConnector = (StaticConnector) connector;
                updateStaticField(staticFieldConnector, connectorModel);
            } else if (connectorModel.getBindingType() == BindingType.DYNAMIC && connector instanceof DynamicConnector) {
                final DynamicConnector dynamicFieldConnector = (DynamicConnector) connector;
                updateDynamicField(dynamicFieldConnector, connectorModel);
            } else {
                throw new LoaderException(
                        String.format("Incompatible match for connector and connectorModel: %s [connector(name)=%s, connectorModel(bindingType)=%s]",
                                fieldName,
                                connector.getClass().getSimpleName(),
                                connectorModel.getBindingType()
                        )
                );
            }
        }
    }

    /**
     * Updates the {@link StaticPin} of {@link StaticConnector} with data from static {@link ConnectorModel}.
     *
     * @param connector      a static connector to be updated; may not be null
     * @param connectorModel an instance of {@link ConnectorModel} that contains value for update
     */
    private void updateStaticField(final StaticConnector connector, final ConnectorModel connectorModel) {
        final var pinModels = databaseManager.dao(PinsDao.class).getByConnectorId(connectorModel.getId());
        // static must contain only one fieldModel, dynamic can have 0..N
        if (pinModels.size() != 1) {
            throw new LoaderException(String.format("Data integrity issue: %s pins fetched although connector is static: %s", pinModels.size(),
                    connectorModel));
        }

        final var pin = connector.getPin();
        final var pinModel = pinModels.iterator().next();

        // override the UID
        final var uid = Objects.requireNonNull(pinModel.getUid());
        pin.setUid(uid);

        // set the last value, if found
        final var lastValue = databaseManager.dao(PinsDao.class).getLastValueById(pinModel.getId());
        if (lastValue != null) {
            pin.setValue(lastValue);
        }

        // log
        LOG.trace("Static Pin {} (connectorId: {}, fieldId: {}, value: {})", pin.getUid(), connectorModel.getId(),
                pinModel.getId(), lastValue);
    }

    /**
     * Updates the {@link DynamicPin} of {@link DynamicConnector} with data from dynamic {@link ConnectorModel}.
     *
     * @param connector      a dynamic connector to be updated; may not be null
     * @param connectorModel an instance of {@link ConnectorModel} containing values for update; may not be null
     */
    private void updateDynamicField(final DynamicConnector connector, final ConnectorModel connectorModel) {
        final var pinModels = databaseManager.dao(PinsDao.class).getByConnectorId(connectorModel.getId());

        // remove all existing
        connector.reset();
        connector.tryIncrease(pinModels.size());
        Preconditions.checkState(connector.size() == pinModels.size(),
                "Could not reset to desired size (actual={}, desired={})", connector.size(), pinModels.size());

        // there may be 0..N
        for (var i = 0; i < pinModels.size(); i++) {
            final var pin = connector.getPin(i);
            final var pinModel = pinModels.get(i);

            // override the UID
            final var uid = Objects.requireNonNull(pinModel.getUid());
            pin.setUid(uid);

            // set the last value, if found
            final var lastValue = databaseManager.dao(PinsDao.class).getLastValueById(pinModel.getId());
            if (lastValue != null) {
                pin.setValue(lastValue);
            }

            // log
            LOG.trace("Dynamic Pin {} (connectorId: {}, fieldId: {}, index: {}, value: {})", pin.getUid(),
                    connectorModel.getId(), pinModel.getId(), pinModel.getIndex(), lastValue);
        }
    }

    /**
     * Loads a class from current class loader and casts to the {@code expectedClass}
     *
     * @param className     the component as string class representation to be loaded; may not be null
     * @param expectedClass the target class to be casted to; may not be null
     * @param <E>           the type of component
     * @return a component instance which is a type of {@code E} class
     * @throws LoaderException in case there was an error during loading or casting
     */
    protected <E> E loadClassAndCast(final String className, final Class<E> expectedClass) {
        final E instance;
        try {
            final var loadedClass = loadClass(className);

            // Meets requirements?
            if (!expectedClass.isAssignableFrom(loadedClass)) {
                throw new InstantiationError("Class '" + className + "' doesn't implements/extends: " + expectedClass);
            }

            final var obj = loadedClass.getConstructor().newInstance();
            instance = expectedClass.cast(obj);
            LOG.debug("New instance created successfully: {}", instance);
        } catch (final ReflectiveOperationException e) {
            throw new LoaderException("Is class an interface or has no non-arg constructor? " +
                    "Could not load class: " + className, e);
        }
        return instance;
    }

    protected Class<?> loadClass(final String className) {
        try {

            return Class.forName(className);
        } catch (final ClassNotFoundException e) {
            throw new LoaderException("Could not find class: " + className, e);
        }
    }
}
