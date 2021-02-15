package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.event.EventKey;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Translates the event key pattern from 'eventKey' column
 * from database to {@link EventKey} enum
 *
 * @author PITSCHR
 */
public class EventKeyColumnMapper implements ColumnMapper<EventKey> {

    @Override
    public EventKey map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        final var eventKeyAsString = r.getString(columnNumber);

        if (Strings.isNullOrEmpty(eventKeyAsString)) {
            return null;
        } else {
            final var eventKeyAsSplitted = eventKeyAsString.split(":", 2);
            Preconditions.checkState(eventKeyAsSplitted.length == 2,
                    "Something went wrong. Expected pattern: <channel>:<identifier> but got: " + eventKeyAsString);

            final var eventKeyChannel = eventKeyAsSplitted[0];
            final var eventKeyIdentifier = eventKeyAsSplitted[1];
            return new EventKey(eventKeyChannel, eventKeyIdentifier);
        }
    }

}
