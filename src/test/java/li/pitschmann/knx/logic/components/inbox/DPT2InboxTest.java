package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT2Inbox} inbox component
 */
public class DPT2InboxTest {

    @Test
    @DisplayName("[KNX] DPT2: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT2Inbox());

        assertThat(inbox.getOutputPins()).hasSize(2);
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("[KNX] DPT2: Not Controlled, TRUE value")
    public void testTrueValue() {
        final var inbox = createInboxComponent(new DPT2Inbox());
        inbox.onNext(new byte[]{0x01});
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(true);
    }


    @Test
    @DisplayName("[KNX] DPT2: Controlled, TRUE value")
    public void testTrueControlledValue() {
        final var inbox = createInboxComponent(new DPT2Inbox());
        inbox.onNext(new byte[]{0x03});
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(true);
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(true);
    }

    @Test
    @DisplayName("[KNX] DPT2: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT2Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x00, 0x11}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT2': 0x00 11");
    }

}
