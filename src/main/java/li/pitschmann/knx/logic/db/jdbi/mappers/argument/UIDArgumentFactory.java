package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Translates the {@link UID} to a {@link Types#VARCHAR}
 *
 * @author PITSCHR
 */
public class UIDArgumentFactory extends AbstractArgumentFactory<UID> {
    public UIDArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(final UID value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.toString());
    }

}
