package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.uid.UID;

/**
 * Database model for {@link Connector} (static and dynamic)
 *
 * @author PITSCHR
 */
public final class ConnectorModel extends Model {
    private UID uid;
    private int componentId;
    private BindingType bindingType;
    private String connectorName;

    /**
     * Creates a new {@link Builder} instance for {@link ConnectorModel}
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public UID getUid() {
        return uid;
    }

    public int getComponentId() {
        return componentId;
    }

    public BindingType getBindingType() {
        return bindingType;
    }

    public boolean isStatic() {
        return bindingType == BindingType.STATIC;
    }

    public boolean isDynamic() {
        return bindingType == BindingType.DYNAMIC;
    }

    public String getConnectorName() {
        return connectorName;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("id", getId()) //
                .add("uid", uid) //
                .add("componentId", componentId) //
                .add("bindingType", bindingType) //
                .add("connectorName", connectorName) //
                .toString();
    }

    /**
     * Builder instance for {@link ComponentModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private UID uid;
        private int componentId;
        private BindingType bindingType;
        private String connectorName;

        public Builder uid(final UID uid) {
            this.uid = uid;
            return this;
        }

        public Builder componentId(final int componentId) {
            this.componentId = componentId;
            return this;
        }

        public Builder bindingType(final Connector connector) {
            if (connector instanceof StaticConnector) {
                bindingType = BindingType.STATIC;
            } else if (connector instanceof DynamicConnector) {
                bindingType = BindingType.DYNAMIC;
            } else {
                throw new IllegalArgumentException("Unsupported connector: " + connector);
            }
            return this;
        }

        public Builder connectorName(final String connectorName) {
            this.connectorName = connectorName;
            return this;
        }

        public ConnectorModel build() {
            final var model = new ConnectorModel();
            model.uid = this.uid;
            model.componentId = this.componentId;
            model.bindingType = this.bindingType;
            model.connectorName = this.connectorName;
            return model;
        }
    }
}
