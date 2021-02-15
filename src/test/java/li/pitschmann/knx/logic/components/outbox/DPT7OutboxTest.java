package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT7Outbox} outbox component
 */
public class DPT7OutboxTest {

    @Test
    @DisplayName("[KNX] DPT7: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT7Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("unsignedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT7: Unsigned Values")
    public void testUnsignedValues() {
        final var outbox = createOutboxComponent(new DPT7Outbox());

        // Value: 23423
        outbox.getInputPin("unsignedValue").setValue(23423);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x5B, 0x7F);

        // Value: 63312
        outbox.getInputPin("unsignedValue").setValue(63312);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xF7, 0x50);
    }

}
