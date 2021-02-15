package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Translates the number taken from a class-named column
 * from database to {@link Class} instance.
 *
 * @author PITSCHR
 */
public class ClassColumnMapper implements ColumnMapper<Class<?>> {

    @Override
    public Class<?> map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        final var classString = r.getString(columnNumber);
        try {
            return classString == null ? null : Class.forName(classString);
        } catch (final ClassNotFoundException ex) {
            throw new SQLException(String.format("Could not find class '%s' to be converted from String.", classString));
        }
    }

}
