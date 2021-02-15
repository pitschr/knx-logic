package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DPT5Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT5Outbox} outbox component
 */
public class DPT5OutboxTest {

    @Test
    @DisplayName("[KNX] DPT5: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT5Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("unsignedValue").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT5: Unsigned Value 47")
    public void testUnsignedValue_47() {
        final var outbox = createOutboxComponent(new DPT5Outbox());

        // Value: 47
        outbox.getInputPin("unsignedValue").setValue(47);
        outbox.execute();
        assertThat(((DPT5Value) outbox.getData()).getValue()).isEqualTo(47);
        assertThat(((DPT5Value) outbox.getData()).toByteArray()).containsExactly(0x2F);
    }

    @Test
    @DisplayName("[KNX] DPT5: Unsigned Value 233")
    public void testUnsignedValue_233() {
        final var outbox = createOutboxComponent(new DPT5Outbox());

        // Value: 233
        outbox.getInputPin("unsignedValue").setValue(233);
        outbox.execute();
        assertThat(((DPT5Value) outbox.getData()).getValue()).isEqualTo(233);
        assertThat(((DPT5Value) outbox.getData()).toByteArray()).containsExactly(0xE9);
    }

}
