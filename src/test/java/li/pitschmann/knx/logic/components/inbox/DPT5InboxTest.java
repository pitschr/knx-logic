package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT5Inbox} inbox component
 */
public class DPT5InboxTest {

    @Test
    @DisplayName("[KNX] DPT5: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT5Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT5: Unsigned Values")
    public void testUnsignedValues() {
        final var inbox = createInboxComponent(new DPT5Inbox());
        inbox.onNext(new byte[]{0x71});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(113);

        inbox.onNext(new byte[]{(byte) 0xFA});
        assertThat(inbox.getOutputPin("unsignedValue").getValue()).isEqualTo(250);
    }

    @Test
    @DisplayName("[KNX] DPT5: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT5Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x33, 0x44}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT5': 0x33 44");
    }

}
