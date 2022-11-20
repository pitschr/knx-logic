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

import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.DiagramsDao;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.uid.UID;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service for {@link Diagram}
 * <p>
 * Persist the diagram in the database.
 */
public final class DiagramService {
    private final DatabaseManager databaseManager;
    private final Router router;

    public DiagramService(final DatabaseManager databaseManager,
                          final Router router) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.router = Objects.requireNonNull(router);
    }

    public void insertDiagram(final Diagram diagram) {
        databaseManager.save(diagram);
    }

    public void updateDiagram(final Diagram diagram) {
        databaseManager.save(diagram);
    }

    public void deleteDiagram(final Diagram diagram) {
        databaseManager.dao(DiagramsDao.class).delete(diagram.getUid());
    }

    public List<Component> getDiagramComponents(final Diagram diagram,
                                                final Function<UID, Component> uidComponentMapper) {
        return databaseManager.dao(ComponentsDao.class)
                .byDiagramUid(diagram.getUid())
                .stream()
                .map(ComponentModel::getUid)
                .map(uidComponentMapper)
                .collect(Collectors.toUnmodifiableList());
    }

    public void deleteDiagramComponent(final Component component) {
        databaseManager.dao(ComponentsDao.class).delete(component.getUid());
        router.deregister(component);
    }
}
