package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests the {@link VariableInbox} inbox component
 */
public class VariableInboxTest {

    @Test
    @DisplayName("[VARIABLE]: Initialization state")
    public void testInitialization() {
        final var inbox = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("data").getValue()).isNull();
    }

    @Test
    @DisplayName("[VARIABLE]: Boolean value")
    public void testBooleanValue() {
        final var inbox = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        inbox.onNext(Boolean.TRUE);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(true);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(Boolean.TRUE);

        inbox.onNext(false);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("[VARIABLE]: Object value")
    public void testObjectValue() {
        final var inbox = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        // String
        inbox.onNext("foobaz");
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo("foobaz");

        // Object
        final var obj = new Object();
        inbox.onNext(obj);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(obj);

        // Byte Array
        final var bytes = new byte[]{6, 7, 8};
        inbox.onNext(bytes);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(bytes);
    }

    @Test
    @DisplayName("[VARIABLE]: Number value")
    public void testNumberValue() {
        final var inbox = new InboxComponentImpl(mock(EventKey.class), new VariableInbox());

        // Integer
        inbox.onNext(17);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(17);

        // Long
        inbox.onNext(89930000L);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(89930000L);

        // Double
        inbox.onNext(3.12d);
        assertThat(inbox.getOutputPin("data").getValue()).isEqualTo(3.12d);
    }

}
