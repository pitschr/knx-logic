package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.value.StepInterval;
import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT3Inbox} inbox component
 */
class DPT3InboxTest {

    @Test
    @DisplayName("[KNX] DPT3: Initialization state")
    void testInitialization() {
        final var inbox = createInboxComponent(new DPT3Inbox());

        assertThat(inbox.getOutputPins()).hasSize(2);
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("stepInterval").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT3: Controlled, Step Interval = PERCENT_25")
    void testControlledStepCode3() {
        final var inbox = createInboxComponent(new DPT3Inbox());
        inbox.onNext(new byte[]{ 0b0000_1011 });
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(true);
        assertThat(inbox.getOutputPin("stepInterval").getValue()).isEqualTo(StepInterval.PERCENT_25);
    }


    @Test
    @DisplayName("[KNX] DPT3: Not Controlled, Step Interval = 7")
    void testNotControlledStepCode7() {
        final var inbox = createInboxComponent(new DPT3Inbox());
        inbox.onNext(new byte[]{ 0b0000_0111 });
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("stepInterval").getValue()).isEqualTo(StepInterval.PERCENT_1);

    }

    @Test
    @DisplayName("[KNX] DPT3: Unsupported value")
    void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT3Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x11, 0x22}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT3': 0x11 22");
    }

}
