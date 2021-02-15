package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT4Inbox} inbox component
 */
public class DPT4InboxTest {

    @Test
    @DisplayName("[KNX] DPT4: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT4Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("charValue").getValue()).isEqualTo(Character.MIN_VALUE);
    }

    @Test
    @DisplayName("[KNX] DPT4: ASCII")
    public void testASCII() {
        final var inbox = createInboxComponent(new DPT4Inbox());
        inbox.onNext(new byte[]{0x47});
        assertThat(inbox.getOutputPin("charValue").getValue()).isEqualTo('G');

        inbox.onNext(new byte[]{0x74});
        assertThat(inbox.getOutputPin("charValue").getValue()).isEqualTo('t');
    }

    @Test
    @DisplayName("[KNX] DPT4: ISO 8859-1")
    public void testISO_8859_1() {
        final var inbox = createInboxComponent(new DPT4Inbox());
        inbox.onNext(new byte[]{(byte) 0xC4});
        assertThat(inbox.getOutputPin("charValue").getValue()).isEqualTo('Ä');

        inbox.onNext(new byte[]{(byte) 0XFF});
        assertThat(inbox.getOutputPin("charValue").getValue()).isEqualTo('ÿ');
    }

    @Test
    @DisplayName("[KNX] DPT4: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT4Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[]{0x22, 0x33}))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT4': 0x22 33");
    }

}
