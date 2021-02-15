package li.pitschmann.knx.logic.components.inbox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPTRawInbox} inbox component
 */
public class DPTRawInboxTest {

    @Test
    @DisplayName("[KNX] Raw: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPTRawInbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("bytes").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] Raw: Empty Byte Array")
    public void testEmptyBytes() {
        final var inbox = createInboxComponent(new DPTRawInbox());

        // empty byte array
        inbox.onNext(new byte[0]);
        assertThat(inbox.getOutputPin("bytes").getValue()).isEqualTo(new byte[0]);
    }

    @Test
    @DisplayName("[KNX] Raw: Non-Empty Byte Array")
    public void testNonEmptyBytes() {
        final var inbox = createInboxComponent(new DPTRawInbox());

        inbox.onNext(new byte[]{0x11, 0x12, 0x13, 0x14, 0x15, 0x16});
        assertThat(inbox.getOutputPin("bytes").getValue()).isEqualTo(new byte[]{0x11, 0x12, 0x13, 0x14, 0x15, 0x16});
    }

}
