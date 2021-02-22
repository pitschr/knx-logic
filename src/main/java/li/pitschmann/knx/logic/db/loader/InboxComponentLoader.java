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

import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.event.EventKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Database mapper to create {@link InboxComponent} out from
 * {@code COMPONENTS} database table.
 * <p>
 * This class will reads following database tables:
 * <ul>
 * <li>components</li>
 * <li>connectors</li>
 * <li>pins</li>
 * </ul>
 */
public class InboxComponentLoader extends AbstractComponentLoader<InboxComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxComponentLoader.class);

    public InboxComponentLoader(final DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public InboxComponent loadById(final int id) {
        final var componentModel = databaseManager.dao(ComponentsDao.class).getById(id);
        final var uid = componentModel.getUid();
        final var className = componentModel.getClassName();

        // load the suitable class
        final Inbox inbox = loadClassAndCast(className, Inbox.class);

        // create event key
        final var eventKeyModel = Objects.requireNonNull(databaseManager.dao(EventKeyDao.class).getByComponentId(id));
        final var eventKey = new EventKey(eventKeyModel.getChannel(), eventKeyModel.getKey());

        // create new component, we will update/wrap in next steps
        final var component = new InboxComponentImpl(eventKey, inbox);

        // set previous UID to make sure that we recognize it as same component
        component.setUid(uid);

        // update connectors (and pins inside)
        final var connectorModels = databaseManager.dao(ConnectorsDao.class).getByComponentId(id);
        if (connectorModels.isEmpty()) {
            LOG.warn("No connectors found for inbox component model?!?! Please double-check your component (id={}): {}", id, className);
        } else if (LOG.isDebugEnabled()) {
            connectorModels.forEach(c -> LOG.debug("{}@{}: {}@{}",
                    className, //
                    c.getComponentId(), //
                    c.getConnectorName(), //
                    c.getId()) //
            );
        }
        updateConnectors(component.getConnectors(), connectorModels);

        return component;
    }
}
