package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;

/**
 * Database model for {@link Pin}
 *
 * @author PITSCHR
 */
public final class PinModel extends Model {
    private UID uid;
    private int connectorId;
    private int index;

    /**
     * Creates a new {@link Builder} instance for {@link PinModel}
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public UID getUid() {
        return uid;
    }

    public int getConnectorId() {
        return connectorId;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("id", getId()) //
                .add("uid", uid) //
                .add("connectorId", connectorId) //
                .add("index", index) //
                .toString();
    }

    /**
     * Builder instance for {@link PinModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private UID uid;
        private int connectorId;
        private int index;

        public Builder uid(final UID uid) {
            this.uid = uid;
            return this;
        }

        public Builder connectorId(final int connectorId) {
            this.connectorId = connectorId;
            return this;
        }

        public Builder index(final int index) {
            this.index = index;
            return this;
        }

        public PinModel build() {
            final var model = new PinModel();
            model.uid = this.uid;
            model.connectorId = this.connectorId;
            model.index = this.index;
            return model;
        }
    }
}
