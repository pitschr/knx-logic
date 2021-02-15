package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPTRawOutbox} outbox component
 */
public class DPTRawOutboxTest {

    @Test
    @DisplayName("[KNX] Raw: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPTRawOutbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("bytes").getValue()).isEqualTo(null);
    }

    @Test
    @DisplayName("[KNX] Raw: Empty Byte Array")
    public void testEmptyBytes() {
        final var outbox = createOutboxComponent(new DPTRawOutbox());

        // null -> empty byte array
        outbox.getInputPin("bytes").setValue(null);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).isEmpty();

        // empty byte array
        outbox.getInputPin("bytes").setValue(new byte[0]);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).isEmpty();
    }

    @Test
    @DisplayName("[KNX] Raw: Non-Empty Byte Array")
    public void testNonEmptyBytes() {
        final var outbox = createOutboxComponent(new DPTRawOutbox());

        outbox.getInputPin("bytes").setValue(new byte[]{0x31, 0x32, 0x33, 0x34, 0x35, 0x36});
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x31, 0x32, 0x33, 0x34, 0x35, 0x36);
    }

}
