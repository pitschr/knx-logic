package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Translates the number taken from 'bindingType' column
 * from database to {@link BindingType} enum
 *
 * @author PITSCHR
 */
public class BindingTypeColumnMapper implements ColumnMapper<BindingType> {

    @Override
    public BindingType map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        return BindingType.valueOf(r.getInt(columnNumber));
    }

}
