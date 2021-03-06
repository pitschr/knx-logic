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
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * {@link PersistenceStrategy} implementation for {@link LogicComponent}
 *
 * @author PITSCHR
 */
public class LogicComponentPersistenceStrategy extends AbstractComponentPersistenceStrategy<LogicComponentImpl> {
    private static final Logger LOG = LoggerFactory.getLogger(LogicComponentPersistenceStrategy.class);
    private final ConnectorPersistence connectorPersistence;
    private final EventKeyPersistence eventKeyPersistence;

    public LogicComponentPersistenceStrategy(final DatabaseManager databaseManager) {
        super(databaseManager);
        this.connectorPersistence = new ConnectorPersistence(databaseManager);
        this.eventKeyPersistence = new EventKeyPersistence(databaseManager);
    }

    @Override
    protected int insert(final LogicComponentImpl component) {
        final var sw = Stopwatch.createStarted();
        LOG.trace("Database write request for logic component: {}", component);

        final var componentModel = ComponentModel.builder()
                .uid(component.getUid())
                .className(component.getWrappedObject().getClass().getName())
                .componentType(ComponentType.LOGIC)
                .build();

        // insert component
        final var componentId = databaseManager.dao(ComponentsDao.class).insert(componentModel);

        // insert connectors and related pins
        connectorPersistence.insertConnectors(componentId, component.getConnectors());

        LOG.info("Logic component for class '{}' written to database (new id={}) and it took {} ms: {}", component.getClass().getName(),
                sw.elapsed(TimeUnit.MILLISECONDS), componentId, component);

        return componentId;
    }

    @Override
    protected int update(final ComponentModel componentModel, final LogicComponentImpl component) {
        final var componentId = componentModel.getId();

        // update connectors and related pins
        connectorPersistence.updateConnectors(componentId, component.getConnectors());

        return componentId;
    }

    @Override
    public Class<?>[] compatibleClasses() {
        return new Class<?>[]{LogicComponentImpl.class};
    }
}
