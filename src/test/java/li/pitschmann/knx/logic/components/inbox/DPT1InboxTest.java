package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT1Inbox} inbox component
 */
public class DPT1InboxTest {

    @Test
    @DisplayName("[KNX] DPT1: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT1Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("[KNX] DPT1: TRUE value")
    public void testTrueValue() {
        final var inbox = createInboxComponent(new DPT1Inbox());
        inbox.onNext(new byte[]{0x01});
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(true);
    }

    @Test
    @DisplayName("[KNX] DPT1: FALSE value")
    public void testFalseValue() {
        final var inbox = createInboxComponent(new DPT1Inbox());
        inbox.onNext(new byte[]{0x00});
        assertThat(inbox.getOutputPin("boolValue").getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("[KNX] DPT1: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT1Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x00, 0x00}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT1': 0x00 00");
    }

}
