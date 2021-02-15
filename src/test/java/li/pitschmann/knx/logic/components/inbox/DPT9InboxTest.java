package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT9Inbox} inbox component
 */
public class DPT9InboxTest {

    @Test
    @DisplayName("[KNX] DPT9: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT9Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(0d);
    }

    @Test
    @DisplayName("[KNX] DPT9: Floating Values (positive)")
    public void testPositiveValues() {
        final var inbox = createInboxComponent(new DPT9Inbox());
        inbox.onNext(new byte[]{0x42, 0x12});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(1356.8d);

        inbox.onNext(new byte[]{0x67, (byte) 0xE0});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(82575.36d);
    }

    @Test
    @DisplayName("[KNX] DPT9: Floating Values (negative)")
    public void testNegativeValues() {
        final var inbox = createInboxComponent(new DPT9Inbox());
        inbox.onNext(new byte[]{(byte) 0xA7, 0x12});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(-38.08d);

        inbox.onNext(new byte[]{(byte) 0xFE, (byte) 0xBC});
        assertThat(inbox.getOutputPin("floatValue").getValue()).isEqualTo(-106168.32d);
    }

    @Test
    @DisplayName("[KNX] DPT9: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT9Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x77}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT9': 0x77");
    }

}
