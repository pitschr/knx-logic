package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT6Inbox} inbox component
 */
public class DPT6InboxTest {

    @Test
    @DisplayName("[KNX] DPT6: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT6Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT6: Signed Values (positive)")
    public void testPositiveValues() {
        final var inbox = createInboxComponent(new DPT6Inbox());
        inbox.onNext(new byte[]{0x01});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(1);

        inbox.onNext(new byte[]{0x45});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(69);
    }

    @Test
    @DisplayName("[KNX] DPT6: Signed Values (negative)")
    public void testNegativeValues() {
        final var inbox = createInboxComponent(new DPT6Inbox());
        inbox.onNext(new byte[]{(byte) 0x87});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-121);

        inbox.onNext(new byte[]{(byte) 0xA4});
        assertThat(inbox.getOutputPin("signedValue").getValue()).isEqualTo(-92);
    }

    @Test
    @DisplayName("[KNX] DPT6: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT6Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x44, 0x55}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT6': 0x44 55");
    }

}
