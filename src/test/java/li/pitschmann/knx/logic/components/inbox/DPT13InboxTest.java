package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT13Inbox} inbox component
 */
public class DPT13InboxTest {

    @Test
    @DisplayName("[KNX] DPT13: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT13Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT13: Signed Values (positive)")
    public void testPositiveValues() {
        final var inbox = createInboxComponent(new DPT13Inbox());
        inbox.onNext(new byte[]{0x03, 0x20, 0x1F, 0x0A});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(52436746);

        inbox.onNext(new byte[]{0x73, 0x21, 0x32, 0x12});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(1931555346);
    }

    @Test
    @DisplayName("[KNX] DPT13: Signed Values (negative)")
    public void testNegativeValues() {
        final var inbox = createInboxComponent(new DPT13Inbox());
        inbox.onNext(new byte[]{(byte) 0xC1, (byte) 0xA1, 0x07, 0x77});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-1046411401);

        inbox.onNext(new byte[]{(byte) 0x80, 0x30, 0x2B, (byte) 0x8A});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-2144326774);
    }

    @Test
    @DisplayName("[KNX] DPT13: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT13Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{(byte) 0x99, (byte) 0xAA}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT13': 0x99 AA");
    }

}
