package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.core.datapoint.value.StepInterval;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT3Outbox} outbox component
 */
class DPT3OutboxTest {

    @Test
    @DisplayName("[KNX] DPT3: Initialization state")
    void testInitialization() {
        final var outbox = createOutboxComponent(new DPT3Outbox());

        assertThat(outbox.getInputPins()).hasSize(2);
        assertThat(outbox.getInputPin("controlled").getValue()).isEqualTo(false);
        assertThat(outbox.getInputPin("stepInterval").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT3: Controlled StepCode")
    void testControlledStepCode() {
        final var outbox = createOutboxComponent(new DPT3Outbox());

        outbox.getInputPin("controlled").setValue(true);

        // Step Interval = STOP
        outbox.getInputPin("stepInterval").setValue(StepInterval.STOP);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0b0000_1000);

        // Step Interval = PERCENT_50
        outbox.getInputPin("stepInterval").setValue(StepInterval.PERCENT_50);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0b0000_1010);
    }

    @Test
    @DisplayName("[KNX] DPT3: Not Controlled Step Code")
    void testNotControlledStepCode() {
        final var outbox = createOutboxComponent(new DPT3Outbox());

        outbox.getInputPin("controlled").setValue(false);

        // Step Interval = PERCENT_12
        outbox.getInputPin("stepInterval").setValue(StepInterval.PERCENT_12);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0b0000_0100);

        // Step Interval = PERCENT_3
        outbox.getInputPin("stepInterval").setValue(StepInterval.PERCENT_3);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0b0000_0110);
    }
}
