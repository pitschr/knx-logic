package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT11Outbox} outbox component
 */
public class DPT11OutboxTest {

    @Test
    @DisplayName("[KNX] DPT11: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT11Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("date").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT11: Date")
    public void testDate() {
        final var outbox = createOutboxComponent(new DPT11Outbox());

        // Value: 1997-11-17
        outbox.getInputPin("date").setValue(LocalDate.of(1997, 11, 17));
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x11, 0x0B, 0x61);

        // Value: 2073-03-30
        outbox.getInputPin("date").setValue(LocalDate.of(2073, 3, 30));
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x1E, 0x03, 0x49);
    }

}
