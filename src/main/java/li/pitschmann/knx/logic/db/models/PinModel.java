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
    private int connectorId;
    private UID uid;
    private int index;

    /**
     * Creates a new {@link Builder} instance for {@link PinModel}
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public int getConnectorId() {
        return this.connectorId;
    }

    public UID getUid() {
        return this.uid;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("id", getId()) //
                .add("connectorId", connectorId) //
                .add("uid", uid) //
                .add("index", index) //
                .toString();
    }

    /**
     * Builder instance for {@link PinModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private int connectorId;
        private UID uid;
        private int index;

        public Builder connectorId(final int connectorId) {
            this.connectorId = connectorId;
            return this;
        }

        public Builder uid(final UID uid) {
            this.uid = uid;
            return this;
        }

        public Builder index(final int index) {
            this.index = index;
            return this;
        }

        public PinModel build() {
            PinModel model = new PinModel();
            model.connectorId = this.connectorId;
            model.uid = this.uid;
            model.index = this.index;
            return model;
        }
    }
}
