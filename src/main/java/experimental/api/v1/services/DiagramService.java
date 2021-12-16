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

import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.diagram.Diagram;

import java.util.Objects;

/**
 * Service for {@link Diagram}
 * <p>
 * Registers/De-registers the diagram in {@link Router} and persists in the database.
 */
public final class DiagramService {
    private final DatabaseManager databaseManager;

    public DiagramService(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    public void insertDiagram(final Diagram diagram) {
        databaseManager.save(diagram);
    }

    public void updateDiagram(final Diagram diagram) {
        databaseManager.save(diagram);
    }
}
