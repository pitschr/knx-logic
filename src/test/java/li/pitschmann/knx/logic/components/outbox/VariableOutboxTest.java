package li.pitschmann.knx.logic.components.outbox;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createOutboxComponent;

/**
 * Tests the {@link VariableOutbox} outbox component
 */
public class VariableOutboxTest {

    @Test
    @DisplayName("[VARIABLE]: Initialization state")
    public void testInitialization() {
        final var outbox = createOutboxComponent(new VariableOutbox());

        assertThat(outbox.getInputConnectors()).hasSize(1);
        assertThat(outbox.getInputPin("data").getValue()).isNull();
    }

    @Test
    @DisplayName("[VARIABLE]: Boolean value")
    public void testBooleanValue() {
        final var outbox = createOutboxComponent(new VariableOutbox());

        // TRUE
        outbox.getInputPin("data").setValue(true);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(true);
        assertThat(outbox.getData()).isEqualTo(Boolean.TRUE);

        // FALSE
        outbox.getInputPin("data").setValue(Boolean.FALSE);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(false);
        assertThat(outbox.getData()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("[VARIABLE]: Object value")
    public void testObjectValue() {
        final var outbox = createOutboxComponent(new VariableOutbox());

        // String
        final var str = "foobar";
        outbox.getInputPin("data").setValue(str);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(str);

        // Object
        final var obj = new Object();
        outbox.getInputPin("data").setValue(obj);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(obj);

        // Byte Array
        final var bytes = new byte[]{2, 3, 4};
        outbox.getInputPin("data").setValue(bytes);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(bytes);
    }

    @Test
    @DisplayName("[VARIABLE]: Number value")
    public void testNumberValue() {
        final var outbox = createOutboxComponent(new VariableOutbox());

        // Integer
        outbox.getInputPin("data").setValue(13);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(13);

        // Long
        outbox.getInputPin("data").setValue(4711L);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(4711L);

        // Double
        outbox.getInputPin("data").setValue(2d);
        outbox.execute();
        assertThat(outbox.getData()).isEqualTo(2.0d);
    }

}
