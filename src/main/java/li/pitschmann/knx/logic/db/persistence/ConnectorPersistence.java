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

package li.pitschmann.knx.logic.db.persistence;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Persist the {@link Connector} to {@link ConnectorModel} in database
 *
 * @author PITSCHR
 */
class ConnectorPersistence {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorPersistence.class);
    private final DatabaseManager databaseManager;
    private final PinPersistence pinPersistence;

    ConnectorPersistence(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.pinPersistence = new PinPersistence(databaseManager);
    }

    /**
     * Tries to find the {@link Pin} by {@link UID} from {@link Connector}
     * <p>
     * <u>Note:</u> We might be able to find the pin using
     * {@link li.pitschmann.knx.logic.connector.ConnectorAware#getPin(UID)}, this however,
     * throws an {@link java.util.NoSuchElementException} if no suitable pin was found.
     * <p>
     * Here it is not desired, as we want to look for limited scope; within {@link Connector} only
     * and if no suitable pin could be found, then a {@code null} should be returned.
     *
     * @param connector the connector that seems holding the pin we are looking for
     * @param uid       the {@link UID} of pin
     * @return the {@link Pin} if found, otherwise {@code null}
     */
    @Nullable
    private static Pin tryGetPinByUid(final Connector connector, final UID uid) {
        return connector.getPinStream().filter(p -> uid.equals(p.getUid())).findFirst().orElse(null);
    }

    /**
     * Inserts list of {@link Connector} for the given component id.
     * <p>
     * It will simply iterate through all connectors and persists
     * each connector and related pins.
     *
     * @param componentId the id of component that is used as owner for connectors
     * @param connectors  list of connectors that should be considered for persistence; may not be null
     */
    public void insertConnectors(final int componentId, final List<Connector> connectors) {
        Preconditions.checkArgument(componentId > 0,
                "Component ID must be positive, but was: {}", componentId);
        for (var connector : connectors) {
            insertConnector(componentId, connector);
        }
    }

    /**
     * Creates a new {@link Connector} in database
     *
     * @param componentId the id of component that is used as owner for connector
     * @param connector   the new connector from component to be updated
     */
    private void insertConnector(final int componentId, final Connector connector) {
        final var connectorModel = toModel(componentId, connector);
        final var connectorId = databaseManager.dao(ConnectorsDao.class).insert(connectorModel);

        final var pins = connector.getPinStream().collect(Collectors.toList());
        pinPersistence.insertPins(connectorId, pins);
    }

    /**
     * Updates list of {@link Connector}
     * <p>
     * This method will also iterate through the list of {@link ConnectorModel} of
     * {@link ComponentModel} and each {@link ConnectorModel} will be cross-checked
     * with list of {@link Connector}.
     * <p>
     * If the given {@link Connector} is not known in database as {@link ConnectorModel} it will be persisted,
     * otherwise it will be updated.
     * </p>
     *
     * @param componentId the identifier of component model
     * @param connectors  list of connectors to be saved; may not be null
     */
    public void updateConnectors(final int componentId, final List<Connector> connectors) {
        Preconditions.checkArgument(componentId > 0,
                "Component ID must be positive, but was: {}", componentId);
        final var connectorModels = databaseManager.dao(ConnectorsDao.class)
                .byComponentId(componentId).stream()
                .collect(
                        Collectors.toUnmodifiableMap(ConnectorModel::getConnectorName, Function.identity())
                );

        for (final var connector : connectors) {
            final var connectorModel = connectorModels.get(connector.getDescriptor().getName());

            // connector doesn't exists in database -> insert connector and pins
            if (connectorModel == null) {
                insertConnector(componentId, connector);
            }
            // dynamic connector exists in database and in component -> update connector and pins
            else if (connector instanceof DynamicConnector) {
                updateDynamicConnector(connectorModel, (DynamicConnector) connector);
            }
            // static connector exists in database and in component -> update connector and pins
            else {
                Preconditions.checkArgument(connector instanceof StaticConnector,
                        "Unsupported Connector. Connector is not an instance of StaticConnector: " + connector);
                updateStaticConnector(connectorModel, (StaticConnector) connector);
            }
        }
    }

    private void updateStaticConnector(final ConnectorModel connectorModel,
                                       final StaticConnector connector) {
        Preconditions.checkArgument(connectorModel.isStatic());

        final var pinsDao = databaseManager.dao(PinsDao.class);
        final var pinModels = pinsDao.getByConnectorId(connectorModel.getId());
        Preconditions.checkState(pinModels.size() == 1,
                "It is expected that static connector has only one pin, but got: " + pinModels);

        final var pinStatic = connector.getPin();
        final var pinModel = pinModels.get(0);
        Preconditions.checkState(pinStatic.getUid().equals(pinModel.getUid()),
                "For any reason the actual pin (name: " + pinStatic.getName() + ", uid: " + pinStatic.getUid() + ")" +
                        " is not matched with the model (id: " + pinModel.getId() + ", uid: " + pinModel.getUid() + ")" +
                        " anymore?!"
        );
    }

    private void updateDynamicConnector(final ConnectorModel connectorModel,
                                        final DynamicConnector connector) {
        Preconditions.checkArgument(connectorModel.isDynamic());

        final var pinsDao = databaseManager.dao(PinsDao.class);
        final var newPinIndexMap = new HashMap<Integer, Integer>();

        // --------------------------------------------
        // Update existing pins in database
        // --------------------------------------------
        final var pinModels = databaseManager.dao(PinsDao.class).getByConnectorId(connectorModel.getId());
        for (final var pinModel : pinModels) {
            final var pin = (DynamicPin) tryGetPinByUid(connector, pinModel.getUid());
            if (pin == null) {
                // pin exists in database, but not in component -> to be removed
                LOG.debug("Dynamic Pin id '{}' (uid: {}) to be removed from connector id '{}' (name: {})",
                        pinModel.getId(), //
                        pinModel.getUid(), //
                        connectorModel.getId(), //
                        connectorModel.getConnectorName() //
                );

                pinsDao.delete(pinModel.getId());
            } else {
                // pin exists in database and in component
                if (pin.getIndex() != pinModel.getIndex()) {
                    // index has been changed -> add new index to the map for batch update
                    newPinIndexMap.put(pinModel.getId(), pin.getIndex());
                }
            }
        }

        // has pin index been changed?
        if (!newPinIndexMap.isEmpty()) {
            LOG.debug("Index of dynamic pins to be updated for connector id '{}' (name: {}): {}",
                    connectorModel.getId(), //
                    connectorModel.getConnectorName(), //
                    newPinIndexMap);

            for (var entry : newPinIndexMap.entrySet()) {
                // simply mark all values as negative so that we don't get constraint violation due
                // Pin ID + Index collision
                pinsDao.updateIndex(entry.getKey(), -entry.getValue());
            }
            for (var entry : newPinIndexMap.entrySet()) {
                // here we set correct index
                pinsDao.updateIndex(entry.getKey(), entry.getValue());
            }
        }

        // --------------------------------------------
        // Insert pins which exists in component but not in database
        // --------------------------------------------
        final var pinModelUIDs = pinModels.stream()
                .map(PinModel::getUid)
                .collect(Collectors.toUnmodifiableSet());

        final var pinsToBeAdded = connector.getPinStream()
                .filter(p -> !pinModelUIDs.contains(p.getUid()))
                .collect(Collectors.toUnmodifiableList());

        if (!pinsToBeAdded.isEmpty()) {
            LOG.debug("Pin to be added for connector id '{}' (name: {}): {}",
                    connectorModel.getId(),
                    connectorModel.getConnectorName(),
                    pinsToBeAdded
            );
            pinPersistence.insertPins(connectorModel.getId(), pinsToBeAdded);
        }
    }

    /**
     * Creates a new {@link ConnectorModel} based on {@code componentId}
     * and {@link Connector} data
     *
     * @param componentId the identifier of component in database
     * @param connector   the connector that is subject to be persisted in the database; may not be null
     * @return a new {@link ConnectorModel}
     */
    private ConnectorModel toModel(final int componentId, final Connector connector) {
        return ConnectorModel.builder()
                .uid(connector.getUid())
                .componentId(componentId)
                .bindingType(connector)
                .connectorName(connector.getDescriptor().getName())
                .build();
    }
}
