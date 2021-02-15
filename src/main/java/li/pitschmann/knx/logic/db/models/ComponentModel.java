package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.uid.UID;

/**
 * Database model for any {@link li.pitschmann.knx.logic.components.Component}
 *
 * @author PITSCHR
 */
public final class ComponentModel extends Model {
    private UID uid;
    private Class<?> className; // cannot rename to 'class' :-(
    private ComponentType componentType;

    /**
     * Creates a new {@link Builder} instance
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public UID getUid() {
        return uid;
    }

    public Class<?> getClassName() {
        return className;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("uid", uid) //
                .add("className", className.getName()) //
                .add("componentType", componentType) //
                .toString();
    }

    /**
     * Creates a new {@link Builder} instance for {@link ComponentModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private UID uid;
        private Class<?> className;
        private ComponentType componentType;

        public Builder uid(final UID uid) {
            this.uid = uid;
            return this;
        }

        public Builder className(final Class<?> className) {
            this.className = className;
            return this;
        }

        public Builder componentType(final ComponentType componentType) {
            this.componentType = componentType;
            return this;
        }

        public ComponentModel build() {
            final var model = new ComponentModel();
            model.uid = this.uid;
            model.className = this.className;
            model.componentType = this.componentType;
            return model;
        }
    }
}
