package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT2Outbox} outbox component
 */
public class DPT2OutboxTest {

    @Test
    @DisplayName("[KNX] DPT2: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT2Outbox());

        assertThat(outbox.getInputPins()).hasSize(2);
        assertThat(outbox.getInputPin("controlled").getValue()).isEqualTo(false);
        assertThat(outbox.getInputPin("boolValue").getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("[KNX] DPT2: TRUE value")
    public void testTrueValue() {
        final var outbox = createOutboxComponent(new DPT2Outbox());

        outbox.getInputPin("boolValue").setValue(true);

        // Not Controlled
        outbox.getInputPin("controlled").setValue(false);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x01);

        // Controlled
        outbox.getInputPin("controlled").setValue(true);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x03);
    }

    @Test
    @DisplayName("[KNX] DPT2: FALSE value")
    public void testFalseValue() {
        final var outbox = createOutboxComponent(new DPT2Outbox());

        outbox.getInputPin("boolValue").setValue(false);

        // Not Controlled
        outbox.getInputPin("controlled").setValue(false);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x00);

        // Controlled
        outbox.getInputPin("controlled").setValue(true);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x02);
    }
}
