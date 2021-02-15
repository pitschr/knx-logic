package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Translates the {@link BindingType} to a {@link Types#TINYINT}
 *
 * @author PITSCHR
 */
public class BindingTypeArgumentFactory extends AbstractArgumentFactory<BindingType> {
    public BindingTypeArgumentFactory() {
        super(Types.TINYINT);
    }

    @Override
    protected Argument build(final BindingType value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setInt(position, value.getIndex());
    }

}
