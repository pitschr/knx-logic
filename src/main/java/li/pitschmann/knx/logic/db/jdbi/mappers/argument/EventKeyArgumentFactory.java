package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.event.EventKey;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Translates the {@link EventKey} to a {@link Types#VARCHAR}
 * with pattern: {@code <channel>:<identifier>}
 *
 * @author PITSCHR
 */
public class EventKeyArgumentFactory extends AbstractArgumentFactory<EventKey> {
    public EventKeyArgumentFactory() {
        super(Types.VARCHAR);
    }

    private static String toString(final EventKey eventKey) {
        return eventKey.getChannel() + ":" + eventKey.getIdentifier();
    }

    @Override
    protected Argument build(final EventKey value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, toString(value));
    }
}
