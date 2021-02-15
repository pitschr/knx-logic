package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.exceptions.DataPointTypeIncompatibleBytesException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createInboxComponent;

/**
 * Tests the {@link DPT16Inbox} inbox component
 */
public class DPT16InboxTest {

    @Test
    @DisplayName("[KNX] DPT16: Initialization state")
    public void testInitialization() {
        final var inbox = createInboxComponent(new DPT16Inbox());

        assertThat(inbox.getOutputPins()).hasSize(1);
        assertThat(inbox.getOutputPin("stringValue").getValue()).isEqualTo("");
    }

    @Test
    @DisplayName("[KNX] DPT16: ASCII")
    public void testASCII() {
        final var inbox = createInboxComponent(new DPT16Inbox());
        inbox.onNext(new byte[]{0x66, 0x6F, 0x6F, 0x62, 0x61, 0x72});
        assertThat(inbox.getOutputPin("stringValue").getValue()).isEqualTo("foobar");

        inbox.onNext(new byte[]{
                0x48, 0x65, 0x6C, 0x6C, 0x6F, // Hello
                0x20,  // space
                0x57, 0x6f, 0x72, 0x6c, 0x64, // World
                0x21 // !
        });
        assertThat(inbox.getOutputPin("stringValue").getValue()).isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("[KNX] DPT16: ISO 8859-1")
    public void testISO_8859_1() {
        final var inbox = createInboxComponent(new DPT16Inbox());
        inbox.onNext(new byte[]{
                (byte) 0xE4, (byte) 0xF6, (byte) 0xFC, // äöü
                (byte) 0xC4, (byte) 0xD6, (byte) 0xDC, // ÄÖÜ
                0x31, 0x32, 0x33, // 123
                (byte) 0xDF // ß
        });
        assertThat(inbox.getOutputPin("stringValue").getValue()).isEqualTo("äöüÄÖÜ123ß");
    }

    @Test
    @DisplayName("[KNX] DPT16: Unsupported value")
    public void testUnsupportedValue() {
        final var inbox = createInboxComponent(new DPT16Inbox());
        assertThatThrownBy(() -> inbox.onNext(new byte[15]))
                .isInstanceOf(DataPointTypeIncompatibleBytesException.class)
                .hasMessage("Looks like you chose a wrong DPT. Given bytes is not compatible for 'DPT16': " +
                        "0x00 00 00 00 00 00 00 00 00 00 00 00 00 00 00");
    }

}
