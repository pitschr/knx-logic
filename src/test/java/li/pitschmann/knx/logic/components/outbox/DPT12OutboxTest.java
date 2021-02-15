package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT12Outbox} outbox component
 */
public class DPT12OutboxTest {

    @Test
    @DisplayName("[KNX] DPT12: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT12Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("unsignedValue").getValue()).isEqualTo(0L);
    }

    @Test
    @DisplayName("[KNX] DPT12: Unsigned Values")
    public void testUnsignedValues() {
        final var outbox = createOutboxComponent(new DPT12Outbox());

        // Value: 733293232
        outbox.getInputPin("unsignedValue").setValue(733293232L);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x2B, 0xB5, 0x2A, 0xB0);

        // Value: 3987332185
        outbox.getInputPin("unsignedValue").setValue(3987332185L);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xED, 0xA9, 0xDC, 0x59);
    }

}
