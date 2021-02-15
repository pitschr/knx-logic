package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.utils.Strings;

import java.util.Objects;

/**
 * Key for Event
 * <p>
 * This class is immutable and represents a key based on channel
 * and the identifier. Each sub-type of {@link EventKey} that contains
 * the same channel and identifier produces the same hash-code and
 * is considered as equal same object.
 *
 * @author PITSCHR
 */
public final class EventKey {
    private final String channel;
    private final String identifier;

    /**
     * <p>Constructor for {@link EventKey} that contains the {@code channel}
     * and {@code identifier} as key for event listeners.</p>
     *
     * <p>There might be same identifier for different channels. But if the
     * identifier is same for the same channel, then it is recognized as
     * the same key in the application.</p>
     *
     * @param channel    the channel of event that belongs to; may not be null
     * @param identifier the identifier of event; may not be null
     */
    public EventKey(final String channel, final String identifier) {
        this.channel = Objects.requireNonNull(channel);
        this.identifier = Objects.requireNonNull(identifier);
    }

    public String getChannel() {
        return channel;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, identifier);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EventKey) {
            final var castedObj = (EventKey) obj;
            return Objects.equals(channel, castedObj.channel)
                    && Objects.equals(identifier, castedObj.identifier);
        }
        return false;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("channel", channel)
                .add("identifier", identifier)
                .toString();
    }
}
