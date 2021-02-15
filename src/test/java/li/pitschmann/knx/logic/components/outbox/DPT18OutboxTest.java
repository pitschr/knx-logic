package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT18Outbox} outbox component
 */
public class DPT18OutboxTest {

    @Test
    @DisplayName("[KNX] DPT18: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT18Outbox());

        assertThat(outbox.getInputPins()).hasSize(2);
        assertThat(outbox.getInputPin("controlled").getValue()).isEqualTo(false);
        assertThat(outbox.getInputPin("sceneNumber").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT18: Not Controlled, Scene Number")
    public void testSceneNumber() {
        final var outbox = createOutboxComponent(new DPT18Outbox());

        // Scene Number: 7
        outbox.getInputPin("sceneNumber").setValue(7);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x07);

        // Scene Number: 58
        outbox.getInputPin("sceneNumber").setValue(58);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x3A);
    }

    @Test
    @DisplayName("[KNX] DPT18: Controlled, Scene Number")
    public void testControlledSceneNumber() {
        final var outbox = createOutboxComponent(new DPT18Outbox());

        outbox.getInputPin("controlled").setValue(true);

        // Controlled Scene Number: 4
        outbox.getInputPin("sceneNumber").setValue(4);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x84);

        // Controlled Scene Number: 61
        outbox.getInputPin("sceneNumber").setValue(61);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0xBD);
    }

}
