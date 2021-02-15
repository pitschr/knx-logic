package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT9Outbox} outbox component
 */
public class DPT9OutboxTest {

    @Test
    @DisplayName("[KNX] DPT9: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT9Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("floatValue").getValue()).isEqualTo(0d);
    }

    @Test
    @DisplayName("[KNX] DPT9: Floating Values (positive)")
    public void testPositiveValues() {
        final var outbox = createOutboxComponent(new DPT9Outbox());

        // Value: 733.321
        outbox.getInputPin("floatValue").setValue(733.321d);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x34, 0x7A);

        // Value: 471433.3443
        outbox.getInputPin("floatValue").setValue(471433.3443d);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x7D, 0x9F);
    }

    @Test
    @DisplayName("[KNX] DPT9: Floating Values (negative)")
    public void testNegativeValues() {
        final var outbox = createOutboxComponent(new DPT9Outbox());

        // Value: -501.000123
        outbox.getInputPin("floatValue").setValue(-501.000123d);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xA9, 0xE2);

        // Value: -640003.278989
        outbox.getInputPin("floatValue").setValue(-640003.278989);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xF8, 0x5F);
    }

}
