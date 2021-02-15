package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT12Inbox} inbox component
 */
public class DPT12InboxTest {

    @Test
    @DisplayName("[KNX] DPT12: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT12Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(0L);
    }

    @Test
    @DisplayName("[KNX] DPT12: Unsigned Values")
    public void testUnsignedValues() {
        final var inbox = createInboxComponent(new DPT12Inbox());
        inbox.onNext(new byte[]{0x00, 0x11, 0x22, 0x33});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(1122867L);

        inbox.onNext(new byte[]{(byte) 0xF0, 0x44, 0x55, 0x66});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(4031010150L);
    }

    @Test
    @DisplayName("[KNX] DPT12: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT12Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x77, (byte) 0x88}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT12': 0x77 88");
    }

}
