package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Translates the number taken from 'componentType' column
 * from database to {@link ComponentType} enum
 *
 * @author PITSCHR
 */
public class ComponentTypeColumnMapper implements ColumnMapper<ComponentType> {

    @Override
    public ComponentType map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        return ComponentType.valueOf(r.getInt(columnNumber));
    }

}
