package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import org.jdbi.v3.core.argument.AbstractArgumentFactory;
import org.jdbi.v3.core.argument.Argument;
import org.jdbi.v3.core.config.ConfigRegistry;

import java.sql.Types;

/**
 * Translates the {@link ComponentType} to a {@link Types#TINYINT}
 *
 * @author PITSCHR
 */
public class ComponentTypeArgumentFactory extends AbstractArgumentFactory<ComponentType> {
    public ComponentTypeArgumentFactory() {
        super(Types.TINYINT);
    }

    @Override
    protected Argument build(final ComponentType value, final ConfigRegistry config) {
        return (position, statement, ctx) -> statement.setInt(position, value.getIndex());
    }

}
