package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT6Outbox} outbox component
 */
public class DPT6OutboxTest {

    @Test
    @DisplayName("[KNX] DPT6: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT6Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT6: Signed Values (positive)")
    public void testPositiveValues() {
        final var outbox = createOutboxComponent(new DPT6Outbox());

        // Value: 31
        outbox.getInputPin("signedValue").setValue(31);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x1F);

        // Value: 102
        outbox.getInputPin("signedValue").setValue(102);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x66);
    }

    @Test
    @DisplayName("[KNX] DPT6: Signed Values (negative)")
    public void testNegativeValues() {
        final var outbox = createOutboxComponent(new DPT6Outbox());

        // Value: -91
        outbox.getInputPin("signedValue").setValue(-91);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xA5);

        // Value: -128
        outbox.getInputPin("signedValue").setValue(-128);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x80);
    }

}
