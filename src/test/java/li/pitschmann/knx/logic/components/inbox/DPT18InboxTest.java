package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT18Inbox} inbox component
 */
public class DPT18InboxTest {

    @Test
    @DisplayName("[KNX] DPT18: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT18Inbox());

        assertThat(inbox.getOutputPins()).hasSize(2);
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT18: Not Controlled, Scene Number")
    public void testSceneNumber() {
        final var inbox = createInboxComponent(new DPT18Inbox());
        inbox.onNext(new byte[]{0x11});
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(17);

        inbox.onNext(new byte[]{0x37});
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(false);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(55);
    }


    @Test
    @DisplayName("[KNX] DPT18: Controlled, Scene Number")
    public void testControlledSceneNumber() {
        final var inbox = createInboxComponent(new DPT18Inbox());
        inbox.onNext(new byte[]{(byte) 0x87}); // 1000 0111
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(true);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(7);

        inbox.onNext(new byte[]{(byte) 0x9B}); // 1001 1011
        assertThat(inbox.getOutputPin("controlled").getValue()).isEqualTo(true);
        assertThat(inbox.getOutputPin("sceneNumber").getValue()).isEqualTo(27);
    }

    @Test
    @DisplayName("[KNX] DPT18: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT18Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x13, 0x14}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT18': 0x13 14");
    }

}
