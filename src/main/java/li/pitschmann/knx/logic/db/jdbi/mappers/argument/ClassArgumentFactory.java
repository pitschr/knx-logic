package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Translates the an instance of {@link Class} to a {@link Types#VARCHAR}
 *
 * @author PITSCHR
 */
public class ClassArgumentFactory extends AbstractArgumentFactory<Class<?>> {
    public ClassArgumentFactory() {
        super(Types.VARCHAR);
    }

    @Override
    protected Argument build(final Class<?> value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setString(position, value.getName());
    }

}
