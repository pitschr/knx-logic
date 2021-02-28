package li.pitschmann.knx.logic.db.jdbi.mappers.row;

import li.pitschmann.knx.logic.exceptions.LoaderException;
import li.pitschmann.knx.logic.transformers.Transformers;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database mapper to return the value from {@code PIN_VALUES} database table.
 */
public class PinValueMapper implements RowMapper<Object> {

    @Override
    public Object map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        final var lastValueString = rs.getString("value");
        final var lastValueClassString = rs.getString("valueType");

        if (lastValueClassString != null) {
            try {
                final var lastValueClass = Class.forName(lastValueClassString);
                return Transformers.transform(lastValueString, lastValueClass);
            } catch (final ClassNotFoundException ex) {
                throw new LoaderException(String.format("Could not find class '%s' to be converted: %s", lastValueClassString, lastValueString));
            }
        }
        return null;
    }

}
