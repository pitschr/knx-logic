package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.components.outbox.VariableOutbox;
import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link OutboxComponentImpl}
 */
class OutboxComponentImplTest {

    @Test
    @DisplayName("Test Outbox Component")
    void testOutboxComponent() {
        final var component = new OutboxComponentImpl(mock(EventKey.class), new VariableOutbox());

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        assertThat(component.getEventKey()).isInstanceOf(EventKey.class);
    }

    @Test
    @DisplayName("Test #toString()")
    void testToString() {
        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.toString()).thenReturn("EVENT-KEY-STRING");

        final var component = new OutboxComponentImpl(eventKeyMock, new VariableOutbox());

        assertThat(component).hasToString(String.format("OutboxComponentImpl" + //
                        "{" + //
                        "uid=%s, " + //
                        "outboxClass=li.pitschmann.knx.logic.components.outbox.VariableOutbox, " + //
                        "eventKey=EVENT-KEY-STRING, " + //
                        "history=%s" + //
                        "}", //
                component.getUid(), component.getHistory())
        );
    }
}
