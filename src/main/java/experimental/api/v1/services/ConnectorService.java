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

package experimental.api.v1.services;

import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.PinLinksDao;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Service for {@link Connector}
 * <p>
 * Registers/De-registers the connector in and persists in the database.
 */
public final class ConnectorService {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorService.class);
    private final DatabaseManager databaseManager;
    private final Router router;

    public ConnectorService(final DatabaseManager databaseManager,
                            final Router router) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.router = Objects.requireNonNull(router);
    }

    /**
     * Adds a new {@link DynamicPin} to the {@link DynamicConnector} at the last index
     *
     * @param connector dynamic connector that should be extended with a pin at the last index; may not be null
     * @return a new created {@link DynamicPin}
     */
    public DynamicPin addPin(final DynamicConnector connector) {
        LOG.debug("Add new pin at last index for connector: {}", connector.getName());
        final var newPin = connector.addPin();

        databaseManager.save(connector);

        // router won't be informed - it will be relevant only when add a link
        // this mechanism is covered by the LinkService
        return newPin;
    }

    /**
     * Adds a new {@link DynamicPin} to the {@link DynamicConnector} at the given {@code index}
     *
     * @param connector dynamic connector that should be extended with a pin at the {@code index}; may not be null
     * @param index     the index where a new {@link Pin} should be added; negative number means at the last index
     * @return a new created {@link DynamicPin}
     */
    public DynamicPin addPin(final DynamicConnector connector, final int index) {
        LOG.debug("Add new pin at index {} for connector: {}", index, connector.getName());
        final var newPin = connector.addPin(index);

        databaseManager.save(connector);

        // router won't be informed - it will be relevant only when add a link
        // this mechanism is covered by the LinkService
        return newPin;
    }

    /**
     * Removes the {@link DynamicPin} at the given {@code index} of {@link DynamicConnector}
     *
     * @param connector dynamic connector that should be altered; may not be null
     * @param index     the index of {@link DynamicPin} that should be removed
     * @return the removed {@link DynamicPin}
     */
    public DynamicPin removePin(final DynamicConnector connector, final int index) {
        final var removedPin = connector.removePin(index);

        databaseManager.save(connector);

        // inform the router because the pin has been removed, therefore,
        // the link needs to be removed as well.
        databaseManager.dao(PinLinksDao.class).delete(removedPin.getUid());
        router.unlink(removedPin);

        return removedPin;
    }

}
