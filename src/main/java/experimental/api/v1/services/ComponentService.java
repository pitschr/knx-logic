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

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Service for {@link Component}
 * <p>
 * Registers/De-registers the components in {@link Router} and persists in the database.
 */
public final class ComponentService {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentService.class);
    private final DatabaseManager databaseManager;
    private final Router router;

    public ComponentService(final DatabaseManager databaseManager,
                            final Router router) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.router = Objects.requireNonNull(router);
    }

    /**
     * Adds and registers a {@link Component} to the logic router and
     * persists it in the database
     *
     * @param component the component to be registered and persisted; may not be null
     */
    public void addComponent(final Component component) {
        Preconditions.checkNonNull(component, "Component is required.");

        router.register(component);
        databaseManager.save(component);
    }

    /**
     * Removes and de-registers the given {@link Component} from the logic router
     * and removes it from the database as well.
     *
     * @param component the component to be de-registered and removed; may not be null
     */
    public void removeComponent(final Component component) {
        Preconditions.checkNonNull(component, "Component is required.");

        router.deregister(component);
        databaseManager.dao(ComponentsDao.class).delete(component.getUid());
    }

    /**
     * Adds a new {@link Pin} to the {@link DynamicConnector} at the last index
     *
     * @param connector dynamic connector that should be extended with a pin at the last index; may not be null
     * @return a new created {@link Pin}
     */
    public Pin addPin(final DynamicConnector connector) {
        return addPin(connector, -1);
    }

    /**
     * Adds a new {@link Pin} to the {@link DynamicConnector} at the given {@code index}
     *
     * @param connector dynamic connector that should be extended with a pin at the {@code index}; may not be null
     * @param index the index where a new {@link Pin} should be added; negative number means at the last index
     *
     * @return a new created {@link Pin}
     */
    public Pin addPin(final DynamicConnector connector, final int index) {
        // if index is provided, then add the pin at the defined index,
        // otherwise add the pin at the end of connector
        final DynamicPin newPin;
        if (index < 0) {
            LOG.debug("Add new pin at last index for connector: {}", connector.getName());
            newPin = connector.addPin();
        } else {
            LOG.debug("Add new pin at index {} for connector: {}", index, connector.getName());
            newPin = connector.addPin(index);
        }

        // update the database (component)
        databaseManager.save(connector);

        // router must not be informed - it will be relevant only when add a link
        // this mechanism is covered by the LinkService
        return newPin;
    }

    /**
     * Removes the {@link Pin} at the given {@code index} of {@link DynamicConnector}
     *
     * @param connector dynamic connector that should be altered; may not be null
     * @param index the index of {@link Pin} that should be removed
     *
     * @return the removed {@link Pin}
     */
    public Pin removePin(final DynamicConnector connector, final int index) {
        final var removedPin = connector.removePin(index);

        // update the database (component)
        databaseManager.save(connector);

        router.unlink(removedPin);

        return removedPin;
    }

}
