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

import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.DiagramsDao;
import li.pitschmann.knx.logic.db.models.DiagramModel;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.diagram.DiagramImpl;

/**
 * Persist the {@link Diagram} to {@link DiagramModel} in database
 *
 * @author PITSCHR
 */
public final class DiagramPersistenceStrategy extends AbstractPersistence<DiagramModel, DiagramImpl> {
    public DiagramPersistenceStrategy(final DatabaseManager databaseManager) {
        super(databaseManager);
    }

    /**
     * Creates a new {@link DiagramModel} based on {@link Diagram}
     *
     * @param diagram the diagram as source for model
     * @return a new {@link DiagramModel}
     */
    protected DiagramModel toModel(final DiagramImpl diagram) {
        return DiagramModel.builder()
                .uid(diagram.getUid())
                .name(diagram.getName())
                .description(diagram.getDescription())
                .build();
    }

    @Override
    public Class<?>[] compatibleClasses() {
        return new Class<?>[]{DiagramImpl.class};
    }

    @Override
    protected Class<DiagramsDao> daoClass() {
        return DiagramsDao.class;
    }
}
