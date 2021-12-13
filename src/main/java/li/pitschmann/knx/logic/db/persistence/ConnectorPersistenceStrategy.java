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

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link PersistenceStrategy} for {@link Connector}
 *
 * @author PITSCHR
 */
public final class ConnectorPersistenceStrategy implements PersistenceStrategy<Connector> {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorPersistenceStrategy.class);
    private final DatabaseManager databaseManager;
    private final ConnectorPersistence connectorPersistence;

    public ConnectorPersistenceStrategy(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.connectorPersistence = new ConnectorPersistence(databaseManager);
    }

    @Override
    public int save(final Connector connector) {
        final var connectorModel = databaseManager.dao(ConnectorsDao.class).find(connector.getUid());

        Preconditions.checkNonNull(connectorModel,
                "This persistence class is suitable for update of connector only. " +
                        "For insert please use a component persistence strategy.");

        connectorPersistence.updateConnectors(
                connectorModel.getComponentId(),
                List.of(connector)
        );

        LOG.debug("Connector saved: {}", connector);
        return connectorModel.getId();
    }

    @Override
    public Class<?>[] compatibleClasses() {
        return new Class<?>[]{
                StaticConnector.class,
                DynamicConnector.class
        };
    }
}
