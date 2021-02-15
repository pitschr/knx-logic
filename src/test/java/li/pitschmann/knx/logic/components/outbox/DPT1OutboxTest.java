package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT1Outbox} outbox component
 */
public class DPT1OutboxTest {

    @Test
    @DisplayName("[KNX] DPT1: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT1Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("boolValue").getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("[KNX] DPT1: TRUE value")
    public void testTrueValue() {
        final var outbox = createOutboxComponent(new DPT1Outbox());

        outbox.getInputPin("boolValue").setValue(true);
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x01);
    }

    @Test
    @DisplayName("[KNX] DPT1: FALSE value")
    public void testFalseValue() {
        final var outbox = createOutboxComponent(new DPT1Outbox());

        outbox.getInputPin("boolValue").setValue(false);
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x00);
    }

}
