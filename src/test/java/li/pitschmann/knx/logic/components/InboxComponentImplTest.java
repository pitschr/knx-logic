package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.components.inbox.VariableInbox;
import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link InboxComponentImpl}
 */
class InboxComponentImplTest {

    @Test
    @DisplayName("Test Inbox Component")
    void testComponentForVariables() {
        final var inboxComponent = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        assertThat(inboxComponent.getEventKey()).isInstanceOf(EventKey.class);
        assertThat(inboxComponent.getOutputConnectors()).hasSize(1);
        assertThat(inboxComponent.getOutputPins()).hasSize(1);
        assertThat(inboxComponent.getOutputPin(0).getConnector()).isEqualTo(inboxComponent.getOutputConnector(0));
    }

    @Test
    @DisplayName("Test Inbox Component using onNext(..)")
    void testInboundWithValue() {
        final var inboxComponent = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        // ---------------------------------------
        // Test
        // ---------------------------------------
        inboxComponent.onNext(1147);

        // ---------------------------------------
        // Verification
        // ---------------------------------------
        assertThat(inboxComponent.getOutputPin("data").getValue()).isEqualTo(1147);
        assertThat(inboxComponent.getHistory().getLast().getValue()).isEqualTo(1147);
    }

    @Test
    @DisplayName("Test #toString()")
    void testToString() {
        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.toString()).thenReturn("EVENT-KEY-STRING");

        final var component = new InboxComponentImpl(eventKeyMock, new VariableInbox());

        assertThat(component).hasToString(String.format("InboxComponentImpl" + //
                        "{" + //
                        "uid=%s, " + //
                        "inboxClass=li.pitschmann.knx.logic.components.inbox.VariableInbox, " + //
                        "eventKey=EVENT-KEY-STRING, " + //
                        "history=%s" + //
                        "}", //
                component.getUid(), component.getHistory())
        );
    }
}
