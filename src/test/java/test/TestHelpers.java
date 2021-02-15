package test;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.components.OutboxComponent;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.components.inbox.VariableInbox;
import li.pitschmann.knx.logic.components.outbox.Outbox;
import li.pitschmann.knx.logic.components.outbox.VariableOutbox;
import li.pitschmann.knx.logic.event.Event;
import li.pitschmann.knx.logic.event.EventKey;
import li.pitschmann.knx.logic.event.VariableEventChannel;

import java.util.UUID;

public final class TestHelpers {
    private TestHelpers() {
    }

    public static InboxComponent createInboxComponent() {
        return new InboxComponentImpl(createEventKey(), new VariableInbox());
    }

    public static InboxComponent createInboxComponent(final String key) {
        return new InboxComponentImpl(createEventKey(String.valueOf(key)), new VariableInbox());
    }

    public static InboxComponent createInboxComponent(final Inbox inbox) {
        return new InboxComponentImpl(createEventKey(), inbox);
    }

    public static OutboxComponent createOutboxComponent() {
        return new OutboxComponentImpl(createEventKey(), new VariableOutbox());
    }

    public static OutboxComponent createOutboxComponent(final String key) {
        return new OutboxComponentImpl(createEventKey(key), new VariableOutbox());
    }

    public static OutboxComponent createOutboxComponent(final Outbox outbox) {
        return new OutboxComponentImpl(createEventKey(), outbox); // key is not test relevant
    }

    public static Event createEvent(final String key, final Object value) {
        return new Event(createEventKey(key), value);
    }

    public static Event createEvent(final InboxComponent inboxComponent, final Object value) {
        return new Event(inboxComponent.getEventKey(), value);
    }

    public static EventKey createEventKey() {
        return createEventKey(String.valueOf(System.nanoTime()));  // key is not test relevant
    }

    public static EventKey createEventKey(final String name) {
        return new EventKey(VariableEventChannel.CHANNEL_ID, name);
    }

    public static LogicComponent createLogicComponent(final Logic logic) {
        return new LogicComponentImpl(logic);
    }

    public static LogicComponent createLogicComponent(final Class<? extends Logic> logicClass) {
        try {
            return createLogicComponent(logicClass.getDeclaredConstructor().newInstance());
        } catch (final ReflectiveOperationException t) {
            throw new AssertionError("Could not create instance from: " + logicClass);
        }
    }
}
