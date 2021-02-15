package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT16Outbox} outbox component
 */
public class DPT16OutboxTest {

    @Test
    @DisplayName("[KNX] DPT16: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT16Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("stringValue").getValue()).isEqualTo("");
    }

    @Test
    @DisplayName("[KNX] DPT16: ASCII")
    public void testASCII() {
        final var outbox = createOutboxComponent(new DPT16Outbox());

        // String: Hello World!
        outbox.getInputPin("stringValue").setValue("Hello World!");
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(
                0x48, 0x65, 0x6C, 0x6C, 0x6F, // Hello
                0x20,  // space
                0x57, 0x6f, 0x72, 0x6c, 0x64, // World
                0x21, // !
                0x00, 0x00 // empty characters to meet 14-bytes byte length
        );
    }

    @Test
    @DisplayName("[KNX] DPT16: ISO 8859-1")
    public void testISO_8859_1() {
        final var outbox = createOutboxComponent(new DPT16Outbox());

        // String: äöüÄÖÜ123ß
        outbox.getInputPin("stringValue").setValue("äöüÄÖÜ123ß");
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(
                (byte) 0xE4, (byte) 0xF6, (byte) 0xFC, // äöü
                (byte) 0xC4, (byte) 0xD6, (byte) 0xDC, // ÄÖÜ
                0x31, 0x32, 0x33, // 123
                (byte) 0xDF, // ß
                0x00, 0x00, 0x00, 0x00 // empty characters to meet 14-bytes byte length
        );
    }

}
