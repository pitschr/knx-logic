package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT11Inbox} inbox component
 */
public class DPT11InboxTest {

    @Test
    @DisplayName("[KNX] DPT11: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT11Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("date").getValue()).isNull();
    }

    @Test
    @DisplayName("[KNX] DPT11: Date")
    public void testDate() {
        final var inbox = createInboxComponent(new DPT11Inbox());
        inbox.onNext(new byte[]{0x02, 0x01, 0x5A});
        assertThat(inbox.getOutputPin("date").getValue()).isEqualTo(LocalDate.of(1990, 1, 2));

        inbox.onNext(new byte[]{0x11, 0x09, 0x14});
        assertThat(inbox.getOutputPin("date").getValue()).isEqualTo(LocalDate.of(2020, 9, 17));

        inbox.onNext(new byte[]{0x1F, 0x0C, 0x59});
        assertThat(inbox.getOutputPin("date").getValue()).isEqualTo(LocalDate.of(2089, 12, 31));
    }

    @Test
    @DisplayName("[KNX] DPT11: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT11Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{(byte) 0x99}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT11': 0x99");
    }

}
