package li.pitschmann.knx.logic.db.strategies;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.db.models.EventKeyModel;
import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.event.EventKey;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Abstract {@link PersistenceStrategy} implementation for {@link Component}
 *
 * @author PITSCHR
 */
abstract class AbstractComponentPersistenceStrategy<T extends Component> implements PersistenceStrategy<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractComponentPersistenceStrategy.class);
    private static final Lock LOCK = new ReentrantLock();
    protected final DatabaseManager databaseManager;

    protected AbstractComponentPersistenceStrategy(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
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
     * Inserts or updates the {@link InboxComponentImpl}.
     *
     * @param component
     */
    @Transaction
    @Override
    public int save(final T component) {
        LOCK.lock();
        try {
            return databaseManager.jdbi().inTransaction(h -> {
                final var dao = databaseManager.dao(ComponentsDao.class);
                var model = dao.getByUID(component.getUid());
                return model == null ? insert(component) : update(model, component);
            });
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Persists given {@link Component} to the database
     *
     * @param component component as source to create a model and insert it
     * @return the new id of component
     */
    protected abstract int insert(final T component);

    /**
     * Persists given {@link Component} to the database
     *
     * @param model     model to be updated
     * @param component component as source to update the model
     * @return the new primary key of component
     */
    protected abstract int update(final ComponentModel model, final T component);

    /**
     * Persists given {@link EventKey} to the database
     *
     * @param componentId the id of component that is used as owner for connector; may not be zero or negative
     * @param eventKey    the event key to be persisted; may not be null
     * @return the new primary key of event key
     */
    protected int insertEventKey(final int componentId,
                                 final EventKey eventKey) {
        Preconditions.checkArgument(componentId > 0);
        final var eventKeyModel = EventKeyModel.builder()
                .componentId(componentId)
                .channel(eventKey.getChannel())
                .key(eventKey.getIdentifier())
                .build();
        return verifyAutoGeneratedKey(databaseManager.dao(EventKeyDao.class).insert(eventKeyModel));
    }

    /**
     * Updates the given {@link EventKey} to the database
     *
     * @param componentModel the {@link ComponentModel} as owner for {@link EventKey}; may not be null
     * @param eventKey       the event key to be updated; may not be null
     */
    protected void updateEventKey(final ComponentModel componentModel,
                                  final EventKey eventKey) {
        Preconditions.checkArgument(componentModel.getId() > 0);
        final var eventKeyModel = EventKeyModel.builder()
                .componentId(componentModel.getId())
                .channel(eventKey.getChannel())
                .key(eventKey.getIdentifier())
                .build();
        databaseManager.dao(EventKeyDao.class).update(eventKeyModel);
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
    protected void insertConnectors(final int componentId, final List<Connector> connectors) {
        Preconditions.checkArgument(componentId > 0);
        for (var connector : connectors) {
            insertConnector(componentId, connector);
        }
    }

    /**
     * Creates a new {@link Connector} in database
     *
     * @param componentId the id of component that is used as owner for connector
     * @param connector   the new connector from component to be updated
     * @return new primary key of persisted connector
     */
    private int insertConnector(final int componentId, final Connector connector) {
        final var descriptor = connector.getDescriptor();
        final var connectorModel = ConnectorModel.builder()
                .componentId(componentId)
                .bindingType(connector)
                .connectorName(descriptor.getName())
                .build();
        final var pins = connector.getPinStream().collect(Collectors.toList());

        final var connectorId = verifyAutoGeneratedKey(databaseManager.dao(ConnectorsDao.class).insert(connectorModel));
        insertPins(connectorId, pins);
        return connectorId;
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
     * @param componentModel the {@link ComponentModel} that should own the connectors; may not be null
     * @param connectors     list of connectors to be saved; may not be null
     */
    protected void updateConnectors(final ComponentModel componentModel, final List<Connector> connectors) {
        final var connectorModels = databaseManager.dao(ConnectorsDao.class)
                .getByComponentId(componentModel.getId()).stream()
                .collect(
                        Collectors.toUnmodifiableMap(ConnectorModel::getConnectorName, Function.identity())
                );

        for (final var connector : connectors) {
            final var connectorModel = connectorModels.get(connector.getDescriptor().getName());

            // connector doesn't exists in database -> insert connector and pins
            if (connectorModel == null) {
                insertConnector(componentModel.getId(), connector);
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

    /*
     * Logic to update the static connector the static {@link Connector}
     */
    private void updateStaticConnector(final ConnectorModel connectorModel,
                                       final StaticConnector connector) {
        Preconditions.checkArgument(connectorModel.isStatic());

        final var pinsDao = databaseManager.dao(PinsDao.class);
        final var pinModels = pinsDao.getByConnectorId(connectorModel.getId());
        Preconditions.checkState(pinModels.size() == 1,
                "It is expected that static connector has only one pin, but got: " + pinModels);

        final var pin = connector.getPin();
        final var pinModel = pinModels.get(0);

        LOG.debug("Value of Static Pin '{}' (value: {}) to be updated for connector id '{}' (name: {}): {}",
                pin.getUid(), //
                pin.getValue(), //
                connectorModel.getId(), //
                connectorModel.getConnectorName(), //
                pin);

        verifyAutoGeneratedKey(pinsDao.insertValue(pinModel.getId(), pin.getValue()));
    }

    /*
     * Logic to update the dynamic connector and also takes care about the
     * re-indexing if position of pins have been changed.
     */
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

                // update the last value
                LOG.debug("Value of Dynamic Pin '{}' (value: {}) to be updated for connector id '{}' (name: {}): {}",
                        pin.getUid(), //
                        pin.getValue(), //
                        connectorModel.getId(), //
                        connectorModel.getConnectorName(), //
                        pin);

                verifyAutoGeneratedKey(pinsDao.insertValue(pinModel.getId(), pin.getValue()));
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
        final var pinModelUIDs = databaseManager.dao(PinsDao.class).getByConnectorId(connectorModel.getId())
                .stream()
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
            insertPins(connectorModel.getId(), pinsToBeAdded);
        }
    }

    /**
     * Saves the list of {@link Pin} for given connector id
     *
     * @param connectorId the primary key of connector in database
     * @param pins        list of {@link Pin} to be saved
     */
    private void insertPins(final int connectorId, final List<Pin> pins) {
        for (final var pin : pins) {
            insertPin(connectorId, pin);
        }
    }

    /**
     * Inserts the pin for given connector id
     *
     * @param connectorId the primary key of connector in database
     * @param pin         the pin to be persisted
     * @return the primary key of pin after insertion
     */
    private int insertPin(final int connectorId, final Pin pin) {
        // index for dynamic may vary, for static it is always a constant
        final var pinIndex = pin instanceof DynamicPin ? ((DynamicPin) pin).getIndex() : 0;

        final var pinModel = PinModel.builder()
                .connectorId(connectorId)
                .uid(pin.getUid())
                .index(pinIndex)
                .build();

        final var pinId = verifyAutoGeneratedKey(databaseManager.dao(PinsDao.class).insert(pinModel));

        final var value = pin.getValue();
        return verifyAutoGeneratedKey(databaseManager.dao(PinsDao.class).insertValue(pinId, value));
    }

}
