package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;

/**
 * Database model for links between two {@link Pin}s
 *
 * @author PITSCHR
 */
public final class PinLinkModel extends Model {
    private int pin1;
    private int pin2;

    /**
     * Creates a new {@link Builder} instance for {@link PinLinkModel}
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public int getPin1() {
        return pin1;
    }

    public int getPin2() {
        return pin2;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("id", getId()) //
                .add("pin1", pin1) //
                .add("pin2", pin2) //
                .toString();
    }

    /**
     * Builder instance for {@link PinLinkModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private int pin1;
        private int pin2;

        public Builder pin1(final int pin1) {
            this.pin1 = pin1;
            return this;
        }

        public Builder pin2(final int pin2) {
            this.pin2 = pin2;
            return this;
        }

        public PinLinkModel build() {
            final var model = new PinLinkModel();
            // ensure that pin1 is always lower than pin2
            // to ensure the unique integrity in database
            // e.g. pin1 = 4, pin2 = 7  -> pin1 = 4, pin2 = 7
            // e.g. pin1 = 7, pin2 = 4  -> pin1 = 4, pin2 = 7
            model.pin1 = Math.min(pin1, pin2);
            model.pin2 = Math.max(pin1, pin2);
            return model;
        }
    }
}
