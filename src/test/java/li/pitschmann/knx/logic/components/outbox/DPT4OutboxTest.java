package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT4Outbox} outbox component
 */
public class DPT4OutboxTest {

    @Test
    @DisplayName("[KNX] DPT4: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT4Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("charValue").getValue()).isEqualTo(Character.MIN_VALUE);
    }

    @Test
    @DisplayName("[KNX] DPT4: ASCII")
    public void testASCII() {
        final var outbox = createOutboxComponent(new DPT4Outbox());

        // Character: K
        outbox.getInputPin("charValue").setValue('K');
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x4B);

        // Character: $ (Dollar Sign)
        outbox.getInputPin("charValue").setValue('$');
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x24);
    }

    @Test
    @DisplayName("[KNX] DPT4: ISO 8859-1")
    public void testISO_8859_1() {
        final var outbox = createOutboxComponent(new DPT4Outbox());

        // Character: Ä
        outbox.getInputPin("charValue").setValue('Ä');
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xC4);

        // Character: £ (Pound Sign)
        outbox.getInputPin("charValue").setValue('£');
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xA3);
    }

}
