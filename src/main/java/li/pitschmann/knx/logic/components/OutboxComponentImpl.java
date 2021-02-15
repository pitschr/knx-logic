package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.components.outbox.Outbox;
import li.pitschmann.knx.logic.event.EventKey;

/**
 * Implementation of {@link OutboxComponent} that wraps an {@link Outbox}
 * object. This component is designed to be generic for every events
 * and {@link Outbox} instances.
 *
 * <pre>{@code
 * final var eventKey = new EventKey("var", "foobar");
 * final var outbox = new VariableOutbox();
 * final var component = new InboxComponentImpl(eventKey, outbox);
 * component.getInputPins();
 * }</pre>
 *
 * @author PITSCHR
 */
public final class OutboxComponentImpl extends AbstractOutboxComponent {
    /**
     * Creates a new wrapper instance of {@link OutboxComponent}
     * for given {@link Outbox} instance
     *
     * @param key    the event key; may not be null
     * @param outbox the {@link Outbox} instance that should be wrapped; may not be null
     */
    public OutboxComponentImpl(final EventKey key, final Outbox outbox) {
        super(key, outbox);
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("uid", getUid())
                .add("outboxClass", getWrappedObject().getClass().getName())
                .add("eventKey", getEventKey())
                .add("history", getHistory())
                .toString();
    }
}
