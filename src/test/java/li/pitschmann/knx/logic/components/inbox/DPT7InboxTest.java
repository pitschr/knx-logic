package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT7Inbox} inbox component
 */
public class DPT7InboxTest {

    @Test
    @DisplayName("[KNX] DPT7: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT7Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT7: Unsigned Values")
    public void testUnsignedValues() {
        final var inbox = createInboxComponent(new DPT7Inbox());
        inbox.onNext(new byte[]{0x71, 0x37});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(28983);

        inbox.onNext(new byte[]{(byte) 0xFA, (byte) 0xEB});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(64235);
    }

    @Test
    @DisplayName("[KNX] DPT7: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT7Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x55}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT7': 0x55");
    }

}
