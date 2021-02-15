package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.event.EventKey;

/**
 * Implementation of {@link InboxComponent} that wraps an {@link Inbox}
 * object. This component is designed to be generic for every events
 * and {@link Inbox} instances.
 *
 * <pre>{@code
 * final var eventKey = new EventKey("var", "foobar");
 * final var inbox = new VariableInbox();
 * final var component = new InboxComponentImpl(eventKey, inbox);
 * component.getOutputPins();
 * }</pre>
 *
 * @author PITSCHR
 */
public final class InboxComponentImpl extends AbstractInboxComponent {
    /**
     * Creates a new wrapper instance of {@link InboxComponent}
     * for given {@link Inbox} instance
     *
     * @param key   the event key; may not be null
     * @param inbox the {@link Inbox} instance that should be wrapped; may not be null
     */
    public InboxComponentImpl(final EventKey key, final Inbox inbox) {
        super(key, inbox);
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("uid", getUid())
                .add("inboxClass", getWrappedObject().getClass().getName())
                .add("eventKey", getEventKey())
                .add("history", getHistory())
                .toString();
    }
}
