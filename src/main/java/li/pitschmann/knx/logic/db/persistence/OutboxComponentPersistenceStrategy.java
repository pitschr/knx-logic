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

import li.pitschmann.knx.core.utils.Stopwatch;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * {@link PersistenceStrategy} implementation for {@link OutboxComponentImpl}
 *
 * @author PITSCHR
 */
public final class OutboxComponentPersistenceStrategy extends AbstractPersistence<ComponentModel, OutboxComponentImpl> {
    private static final Logger LOG = LoggerFactory.getLogger(OutboxComponentPersistenceStrategy.class);
    private final ConnectorPersistence connectorPersistence;
    private final EventKeyPersistence eventKeyPersistence;

    public OutboxComponentPersistenceStrategy(final DatabaseManager databaseManager) {
        super(databaseManager);
        this.connectorPersistence = new ConnectorPersistence(databaseManager);
        this.eventKeyPersistence = new EventKeyPersistence(databaseManager);
    }

    @Override
    protected int insert(final OutboxComponentImpl component) {
        final var sw = Stopwatch.createStarted();
        LOG.trace("Database write request for outbox component: {}", component);

        // insert component
        final var componentId = super.insert(component);

        // insert connectors and related pins
        connectorPersistence.insertConnectors(componentId, component.getInputConnectors());

        // insert event key
        eventKeyPersistence.insertEventKey(componentId, component.getEventKey());

        LOG.info("Outbox component for class '{}' written to database (new id={}) and it took {} ms: {}", component.getClass().getName(),
                sw.elapsed(TimeUnit.MILLISECONDS), componentId, component);

        return componentId;
    }

    @Override
    protected void update(final int id, final OutboxComponentImpl component) {
        // update connectors and related pins
        connectorPersistence.updateConnectors(id, component.getInputConnectors());

        // update the event key model
        eventKeyPersistence.updateEventKey(id, component.getEventKey());
    }

    @Override
    protected ComponentModel toModel(final OutboxComponentImpl component) {
        return ComponentModel.builder()
                .uid(component.getUid())
                .className(component.getWrappedObject().getClass().getName())
                .componentType(ComponentType.OUTBOX)
                .build();

    }

    @Override
    public Class<?>[] compatibleClasses() {
        return new Class<?>[]{OutboxComponentImpl.class};
    }

    @Override
    protected Class<ComponentsDao> daoClass() {
        return ComponentsDao.class;
    }
}
