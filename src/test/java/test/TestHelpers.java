package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.javalin.http.Context;
import io.javalin.http.util.ContextUtil;
import io.javalin.plugin.json.JavalinJson;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public final class TestHelpers {
    private static final Gson GSON = new GsonBuilder().create();

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

    /**
     * Initializes Javalin JSON with {@link #GSON}
     */
    public static void initJavalinJson() {
        JavalinJson.setFromJsonMapper(GSON::fromJson);
        JavalinJson.setToJsonMapper(GSON::toJson);
    }

    /**
     * Returns a new Javalin {@link Context} incl. wrapped
     * spy-functionality from Mockito
     *
     * @return wrapped {@link Context} with {@link org.mockito.Spy}
     */
    public static Context contextSpy() {
        return spy(ContextUtil.init(mock(HttpServletRequest.class), mock(HttpServletResponse.class)));
    }

    /**
     * Returns a JSON string for given objects in the parameter
     *
     * @param obj the first object; may not be null
     * @param moreObjects the more objects; may be null
     * @return a JSON string representation
     */
    public static String toJson(final Object obj, final Object... moreObjects) {
        Objects.requireNonNull(obj);
        if (moreObjects.length == 0) {
            return GSON.toJson(obj);
        } else {
            final var sb = new StringBuilder(1024);
            sb.append('[').append(GSON.toJson(obj));
            for (var moreObject : moreObjects) {
                sb.append(',').append(toJson(moreObject));
            }
            sb.append(']');
            return sb.toString();
        }
    }
}
