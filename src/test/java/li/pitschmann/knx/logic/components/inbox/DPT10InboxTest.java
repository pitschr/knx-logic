package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT10Inbox} inbox component
 */
public class DPT10InboxTest {

    @Test
    @DisplayName("[KNX] DPT10: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT10Inbox());

        assertThat(inbox.getOutputPins()).hasSize(2);
        assertThat(inbox.getOutputPin("dayOfWeek").getValue()).isNull();
        assertThat(inbox.getOutputPin("time").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT10: Time without Day of Week")
    public void testTime() {
        final var inbox = createInboxComponent(new DPT10Inbox());
        inbox.onNext(new byte[]{0x07, 0x08, 0x09});
        assertThat(inbox.getOutputPin("dayOfWeek").getValue()).isNull();
        assertThat(inbox.getOutputPin("time").getValue()).isEqualTo(LocalTime.of(7, 8, 9));

        inbox.onNext(new byte[]{0x17, 0x30, 0x1F});
        assertThat(inbox.getOutputPin("dayOfWeek").getValue()).isNull();
        assertThat(inbox.getOutputPin("time").getValue()).isEqualTo(LocalTime.of(23, 48, 31));
    }

    @Test
    @DisplayName("[KNX] DPT10: Time with Day of Week")
    public void testTimeWithDayOfWeek() {
        final var inbox = createInboxComponent(new DPT10Inbox());
        inbox.onNext(new byte[]{0x4C, 0x0B, 0x0A});
        assertThat(inbox.getOutputPin("dayOfWeek").getValue()).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(inbox.getOutputPin("time").getValue()).isEqualTo(LocalTime.of(12, 11, 10));

        inbox.onNext(new byte[]{(byte) 0xD1, 0x39, 0x16});
        assertThat(inbox.getOutputPin("dayOfWeek").getValue()).isEqualTo(DayOfWeek.SATURDAY);
        assertThat(inbox.getOutputPin("time").getValue()).isEqualTo(LocalTime.of(17, 57, 22));
    }

    @Test
    @DisplayName("[KNX] DPT10: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT10Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{(byte) 0x88}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT10': 0x88");
    }

}
