package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT17Inbox} inbox component
 */
public class DPT17InboxTest {

    @Test
    @DisplayName("[KNX] DPT17: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT17Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT17: Scene Number")
    public void testSceneNumber() {
        final var inbox = createInboxComponent(new DPT17Inbox());
        inbox.onNext(new byte[]{0x22});
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(34);

        inbox.onNext(new byte[]{0x3F});
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(63);
    }

    @Test
    @DisplayName("[KNX] DPT17: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT17Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x12, 0x13}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT17': 0x12 13");
    }

}
