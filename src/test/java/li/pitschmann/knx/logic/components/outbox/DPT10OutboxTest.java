package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT10Outbox} outbox component
 */
public class DPT10OutboxTest {

    @Test
    @DisplayName("[KNX] DPT10: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT10Outbox());

        assertThat(outbox.getInputPins()).hasSize(2);
        assertThat(outbox.getInputPin("dayOfWeek").getValue()).isNull();
        assertThat(outbox.getInputPin("time").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT10: Time without Day of Week")
    public void testTime() {
        final var outbox = createOutboxComponent(new DPT10Outbox());

        // Value: NO WEEKDAY, 08:19:40
        outbox.getInputPin("time").setValue(LocalTime.of(8, 19, 40));
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x08, 0x13, 0x28);

        // Value: NO WEEKDAY, 17:27:21
        outbox.getInputPin("time").setValue(LocalTime.of(17, 27, 21));
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x11, 0x1B, 0x15);
    }

    @Test
    @DisplayName("[KNX] DPT10: Time with Day of Week")
    public void testTimeWithDayOfWeek() {
        final var outbox = createOutboxComponent(new DPT10Outbox());

        // Value: TUESDAY, 09:16:39
        outbox.getInputPin("dayOfWeek").setValue(DayOfWeek.TUESDAY);                        // 010. ....
        outbox.getInputPin("time").setValue(LocalTime.of(9, 16, 39)); // ...0 1001
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x49, 0x10, 0x27);

        // Value: FRIDAY, 18:54:02
        outbox.getInputPin("dayOfWeek").setValue(DayOfWeek.FRIDAY);                         // 101. ....
        outbox.getInputPin("time").setValue(LocalTime.of(18, 54, 2)); // ...1 0010
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xB2, 0x36, 0x02);
    }

}
