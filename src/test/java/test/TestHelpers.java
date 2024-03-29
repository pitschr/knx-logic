/*
 * Copyright (C) 2022 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public final class TestHelpers {
    private static final Gson GSON = new GsonBuilder().create();
    private static final String UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
    private static final String UUID_PATTERN_REPLACE = "********-****-****-****-************";

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
     * @param obj         the first object; may not be null
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

    /**
     * Asserts the given response body of {@link Context} (which is JSON) against
     * the error {@code message}.
     *
     * @param ctx     the {@link Context} to be verified; may not be null
     * @param message the expected error message
     */
    public static void assertContextJsonErrorMessage(final Context ctx, final String message) {
        assertContextJsonResult(ctx, "{\"message\":\"" + message + "\"}");
    }

    /**
     * Asserts the given response body of {@link Context} (which is JSON) against
     * the {@code expectedJson}. As {@link li.pitschmann.knx.logic.uid.UID} are
     * dynamically, it will be replaced by a dummy UID pattern for comparison.
     *
     * @param ctx          the {@link Context} to be verified; may not be null
     * @param expectedJson the expected JSON string
     */
    public static void assertContextJsonResult(final Context ctx, final String expectedJson) {
        assertContextJsonResult(ctx, expectedJson, true);
    }

    /**
     * Asserts the given response body of {@link Context} (which is JSON) against
     * the {@code expectedJson}. As {@link li.pitschmann.knx.logic.uid.UID} are
     * dynamically, it will be replaced by a dummy UID pattern for comparison.
     *
     * @param ctx          the {@link Context} to be verified; may not be null
     * @param expectedJson the expected JSON string
     * @param isStrict     if the order in array is considered as strict
     */
    public static void assertContextJsonResult(final Context ctx, final String expectedJson, final boolean isStrict) {
        final var actualJson = ctx.resultString();
        if (actualJson == null) {
            assertThat(expectedJson).isNull();
        } else {
            final var expectedJsonWithDummyUid = expectedJson.replaceAll(UUID_PATTERN, UUID_PATTERN_REPLACE);
            final var actualJsonWithDummyUid = actualJson.replaceAll(UUID_PATTERN, UUID_PATTERN_REPLACE);
            final var expectedJsonElement = JsonParser.parseString(expectedJsonWithDummyUid);
            final var actualJsonElement = JsonParser.parseString(actualJsonWithDummyUid);

            if (!isStrict && expectedJsonElement.isJsonArray() && actualJsonElement.isJsonArray()) {
                final var expectedArray = (JsonArray) expectedJsonElement;
                final var actualArray = (JsonArray) actualJsonElement;
                assertThat(expectedArray.size()).isEqualTo(actualArray.size());
                for (var i = 0; i < expectedArray.size(); i++) {
                    assertThat(actualArray.contains(expectedArray.get(i))).isTrue();
                }
            } else {
                assertThat(actualJsonElement).isEqualTo(expectedJsonElement);
            }
        }
    }

    /**
     * Asserts the given response body of {@link Context} (which is usually JSON)
     * if it has no response
     *
     * @param ctx the {@link Context} to be verified; may not be null
     */
    public static void assertContextHasNoResponse(final Context ctx) {
        assertThat(ctx.resultString()).isNull();
    }

    /**
     * Asserts the given response body of {@link Context} (which is JSON) against
     * the content of given {@link Path}. As {@link li.pitschmann.knx.logic.uid.UID}
     * are dynamically, it will be replaced by a dummy UID pattern for comparison.
     *
     * @param ctx  the {@link Context} to be verified; may not be null
     * @param path the path of file that contains the expected JSON; may not be null
     */
    public static void assertContextJsonResult(final Context ctx, final Path path) {
        assertContextJsonResult(ctx, path, true);
    }

    /**
     * Asserts the given response body of {@link Context} (which is JSON) against
     * the content of given {@link Path}. As {@link li.pitschmann.knx.logic.uid.UID}
     * are dynamically, it will be replaced by a dummy UID pattern for comparison.
     *
     * @param ctx      the {@link Context} to be verified; may not be null
     * @param path     the path of file that contains the expected JSON; may not be null
     * @param isStrict if the order in array is considered as strict
     */
    public static void assertContextJsonResult(final Context ctx, final Path path, final boolean isStrict) {
        final var newPath = TestHelpers.class.getResource("/").getPath() + path.toString();
        try {
            final var expectedJson = Files.readString(Path.of(newPath));
            assertContextJsonResult(ctx, expectedJson, isStrict);
        } catch (IOException e) {
            fail("Could not read string from path: " + newPath, e);
        }
    }
}
