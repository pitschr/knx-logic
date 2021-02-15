package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT8Inbox} inbox component
 */
class DPT8InboxTest {

    @Test
    @DisplayName("[KNX] DPT8: Initialization state")
    void testInitialization() {
        final var inbox = createInboxComponent(new DPT8Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT8: Signed Values (positive)")
    void testPositiveValues() {
        final var inbox = createInboxComponent(new DPT8Inbox());
        inbox.onNext(new byte[]{0x77, 0x23});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(30499);

        inbox.onNext(new byte[]{0x54, (byte) 0x89});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(21641);
    }

    @Test
    @DisplayName("[KNX] DPT8: Signed Values (negative)")
    void testNegativeValues() {
        final var inbox = createInboxComponent(new DPT8Inbox());
        inbox.onNext(new byte[]{(byte) 0xA5, 0x43});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-23229);

        inbox.onNext(new byte[]{(byte) 0xF2, (byte) 0xCA});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-3382);
    }

    @Test
    @DisplayName("[KNX] DPT8: Unsupported value")
    void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT8Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x66}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT8': 0x66");
    }

}
