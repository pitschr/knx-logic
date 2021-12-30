package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.pin.Pin;

/**
 * Database model for links between two {@link Pin}s
 * <p>
 * It doesn't matter if the id of pin1 or pin2 is higher/lower.
 * Internally the pin1 is stored with lower value, while pin2
 * has higher value. In this way it is ensured that there
 * is no database conflict when id of pin1 and pin2 are swapped.
 *
 * @author PITSCHR
 */
public final class PinLinkModel extends Model {
    private int pin1;
    private int pin2;

    /**
     * Creates a new {@link PinLinkModel}
     *
     * @param pin1 the first pin id
     * @param pin2 the second pin id
     * @return {@link PinLinkModel}
     */
    public static PinLinkModel create(final int pin1, final int pin2) {
        final var model = new PinLinkModel();
        // ensure that pin1 is always lower than pin2
        // to ensure the unique integrity in database
        // e.g. pin1 = 4, pin2 = 7  -> pin1 = 4, pin2 = 7
        // e.g. pin1 = 7, pin2 = 4  -> pin1 = 4, pin2 = 7
        model.pin1 = Math.min(pin1, pin2);
        model.pin2 = Math.max(pin1, pin2);
        return model;
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
}
