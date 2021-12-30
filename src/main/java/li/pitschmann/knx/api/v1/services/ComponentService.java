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

package li.pitschmann.knx.api.v1.services;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;

import java.util.Objects;

/**
 * Service for {@link Component}
 * <p>
 * Registers/De-registers the components in {@link Router} and persists in the database.
 */
public final class ComponentService {
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

        databaseManager.save(component);
        router.register(component);
    }

    /**
     * Removes and de-registers the given {@link Component} from the logic router
     * and removes it from the database as well.
     *
     * @param component the component to be de-registered and removed; may not be null
     */
    public void removeComponent(final Component component) {
        Preconditions.checkNonNull(component, "Component is required.");

        databaseManager.dao(ComponentsDao.class).delete(component.getUid());
        router.deregister(component);
    }

}
