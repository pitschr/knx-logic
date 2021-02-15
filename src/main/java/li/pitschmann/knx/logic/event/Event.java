package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.utils.ByteFormatter;
import li.pitschmann.knx.core.utils.Strings;

import java.util.Objects;

/**
 * The {@link Event} data object to be transferred.
 * <p>
 * This class is immutable.
 *
 * @author PITSCHR
 */
public final class Event {
    private final EventKey key;
    private final Object value;

    /**
     * Creates an Event element that contains {@link EventKey} and
     * the value to be transferred.
     *
     * @param key   the event key that this event belongs to; may not be null
     * @param value the value of event to be read and transferred; may not be null
     */
    public Event(final EventKey key, final Object value) {
        this.key = Objects.requireNonNull(key);
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns the key of event
     *
     * @return an {@link EventKey}; may not be null
     */
    public EventKey getKey() {
        return key;
    }

    /**
     * Returns the data
     *
     * @return data; may not be null
     */
    public Object getData() {
        return value;
    }

    @Override
    public String toString() {
        final var helper = Strings.toStringHelper(this)
                .add("channel", key.getChannel())
                .add("identifier", key.getIdentifier());

        if (value instanceof byte[]) {
            helper.add("data", ByteFormatter.formatHexAsString((byte[]) value));
        } else {
            helper.add("data", value);
        }

        return helper.toString();
    }
}
