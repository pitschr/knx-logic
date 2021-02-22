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

import experimental.api.ComponentFactory;
import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Database mapper to create {@link LogicComponent} out from
 * {@code COMPONENTS} database table.
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
public class LogicComponentLoader extends AbstractComponentLoader<LogicComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(LogicComponentLoader.class);
    private final ComponentFactory componentFactory;

    public LogicComponentLoader(final DatabaseManager databaseManager,
                                final ComponentFactory componentFactory) {
        super(databaseManager);
        this.componentFactory = Objects.requireNonNull(componentFactory);
    }

    @Override
    public LogicComponent loadById(final int id) {
        final var componentModel = databaseManager.dao(ComponentsDao.class).getById(id);
        final var uid = componentModel.getUid();
        final var className = componentModel.getClassName();

        final var logic = loadClassAndCast(componentModel.getClassName(), Logic.class);
        final var component = new LogicComponentImpl(logic);

        // set previous UID to make sure that we recognize it as same component
        component.setUid(uid);

        // update connectors (and pins inside)
        final var connectorModels = databaseManager.dao(ConnectorsDao.class).getByComponentId(id);
        if (connectorModels.isEmpty()) {
            LOG.warn("No connectors found for logic component model?!?! Please double-check your component (id={}): {}", id, className);
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

    @Override
    protected Class<?> loadClass(String className) {
        try {
            return super.loadClass(className);
        } catch (final Exception ex) {
            return componentFactory.getLogicRepository().findLogicClass(className);
        }
    }
}
