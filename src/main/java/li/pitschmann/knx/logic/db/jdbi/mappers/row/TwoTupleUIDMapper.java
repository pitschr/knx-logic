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

package li.pitschmann.knx.logic.db.jdbi.mappers.row;

import li.pitschmann.knx.logic.db.jdbi.mappers.data.TwoTupleUID;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper for {@link TwoTupleUID} containing
 * two {@link li.pitschmann.knx.logic.uid.UID}s to be returned
 */
public class TwoTupleUIDMapper implements RowMapper<TwoTupleUID> {

    @Override
    public TwoTupleUID map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new TwoTupleUID(
                UIDFactory.createUid(rs.getString(1)),
                UIDFactory.createUid(rs.getString(2))
        );
    }
}
