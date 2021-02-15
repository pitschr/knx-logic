package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Translates the uid columns from database to a {@link UID}
 *
 * @author PITSCHR
 */
public class UIDColumnMapper implements ColumnMapper<UID> {

    @Override
    public UID map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        return UIDFactory.createUid(r.getString(columnNumber));
    }

}
