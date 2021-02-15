package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT8Outbox} outbox component
 */
class DPT8OutboxTest {

    @Test
    @DisplayName("[KNX] DPT8: Initialization state")
    void testInitialization() {
        final var outbox = createOutboxComponent(new DPT8Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("signedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT8: Signed Values (positive)")
    void testPositiveValues() {
        final var outbox = createOutboxComponent(new DPT8Outbox());

        // Value: 782
        outbox.getInputPin("signedValue").setValue(782);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x03, 0x0E);

        // Value: 31233
        outbox.getInputPin("signedValue").setValue(31233);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x7A, 0x01);
    }

    @Test
    @DisplayName("[KNX] DPT8: Signed Values (negative)")
    void testNegativeValues() {
        final var outbox = createOutboxComponent(new DPT8Outbox());

        // Value: -23501
        outbox.getInputPin("signedValue").setValue(-23501);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xA4, 0x33);

        // Value: -32238
        outbox.getInputPin("signedValue").setValue(-32238);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x82, 0x12);
    }

}
