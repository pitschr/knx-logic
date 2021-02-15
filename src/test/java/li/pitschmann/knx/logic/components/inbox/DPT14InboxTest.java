package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT14Inbox} inbox component
 */
public class DPT14InboxTest {

    @Test
    @DisplayName("[KNX] DPT14: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT14Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(0d);
    }

    @Test
    @DisplayName("[KNX] DPT14: Floating Values (positive)")
    public void testPositiveValues() {
        final var inbox = createInboxComponent(new DPT14Inbox());
        inbox.onNext(new byte[]{0x4F, 0x22, 0x56, (byte) 0xF1});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(2.723606784E9d);

        inbox.onNext(new byte[]{0x7F, 0x55, 0x66, (byte) 0xE0});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(2.8365972064150885E38);
    }

    @Test
    @DisplayName("[KNX] DPT14: Floating Values (negative)")
    public void testNegativeValues() {
        final var inbox = createInboxComponent(new DPT14Inbox());
        inbox.onNext(new byte[]{(byte) 0xA5, 0x11, 0x12, 0x13});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(-1.2582868984342704E-16);

        inbox.onNext(new byte[]{(byte) 0xFC, (byte) 0xBC, 0x74, 0x14});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(-7.828049227854572E36);
    }

    @Test
    @DisplayName("[KNX] DPT14: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT14Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[5]))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT14': 0x00 00 00 00 00");
    }

}
