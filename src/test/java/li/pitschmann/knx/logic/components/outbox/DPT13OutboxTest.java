package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT13Outbox} outbox component
 */
public class DPT13OutboxTest {

    @Test
    @DisplayName("[KNX] DPT13: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT13Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT13: Signed Values (positive)")
    public void testPositiveValues() {
        final var outbox = createOutboxComponent(new DPT13Outbox());

        // Value: 54433
        outbox.getInputPin("signedValue").setValue(54433);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x00, 0x00, 0xD4, 0xA1);

        // Value: 98765432
        outbox.getInputPin("signedValue").setValue(98765432);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x05, 0xE3, 0x0A, 0x78);
    }

    @Test
    @DisplayName("[KNX] DPT13: Signed Values (negative)")
    public void testNegativeValues() {
        final var outbox = createOutboxComponent(new DPT13Outbox());

        // Value: -3278
        outbox.getInputPin("signedValue").setValue(-3278);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xFF, 0xFF, 0xF3, 0x32);

        // Value: -164375677
        outbox.getInputPin("signedValue").setValue(-164375677);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xF6, 0x33, 0xD3, 0x83);
    }

}
