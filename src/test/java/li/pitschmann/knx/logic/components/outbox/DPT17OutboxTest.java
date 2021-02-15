package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link DPT17Outbox} outbox component
 */
public class DPT17OutboxTest {

    @Test
    @DisplayName("[KNX] DPT17: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new DPT17Outbox());

        assertThat(outbox.getInputPins()).hasSize(1);
        assertThat(outbox.getInputPin("sceneNumber").getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("[KNX] DPT17: Scene Number")
    public void testSceneNumber() {
        final var outbox = createOutboxComponent(new DPT17Outbox());

        // Scene Number: 8
        outbox.getInputPin("sceneNumber").setValue(8);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x08);

        // Scene Number: 47
        outbox.getInputPin("sceneNumber").setValue(47);
        outbox.execute();
        assertThat(((DataPointValue) outbox.getData()).toByteArray()).containsExactly(0x2F);
    }

}
