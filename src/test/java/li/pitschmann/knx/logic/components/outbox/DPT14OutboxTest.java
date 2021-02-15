package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT14Outbox} outbox component
 */
public class DPT14OutboxTest {

    @Test
    @DisplayName("[KNX] DPT14: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT14Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("floatValue").getValue()).isEqualTo(0d);
    }

    @Test
    @DisplayName("[KNX] DPT14: Floating Values (positive)")
    public void testPositiveValues() {
        final var outbox = createOutboxComponent(new DPT14Outbox());

        // Value: 3.74432123123E12
        outbox.getInputPin("floatValue").setValue(3.74432123123E12);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x54, 0x59, 0xF2, 0xBC);

        // Value: 9.2334443322E36
        outbox.getInputPin("floatValue").setValue(9.2334443322E36);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x7C, 0xDE, 0x49, 0x7F);
    }

    @Test
    @DisplayName("[KNX] DPT14: Floating Values (negative)")
    public void testNegativeValues() {
        final var outbox = createOutboxComponent(new DPT14Outbox());

        // Value: -7.2343233222E-7
        outbox.getInputPin("floatValue").setValue(-7.2343233222E-7);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xB5, 0x42, 0x31, 0xE4);

        // Value: -640003.278989
        outbox.getInputPin("floatValue").setValue(-6.77323211E24);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xE8, 0xB3, 0x49, 0x35);
    }

}
