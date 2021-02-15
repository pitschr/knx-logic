package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.components.outbox.Outbox;
import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link AbstractOutboxComponent}
 */
class AbstractOutboxComponentTest {

    @Test
    @DisplayName("Test Abstract Outbox Component")
    void testComponent() {
        final var component = new TestOutboxComponent();

        assertThat(component.getUid()).isNotNull();
        assertThat(component.getInputConnectors()).hasSize(3);
        assertThat(component.getInputPins()).hasSize(3);
        assertThat(component.getInputPin("input1").getValue()).isEqualTo("");
        assertThat(component.getInputPin("input2").getValue()).isEqualTo("");
        assertThat(component.getInputPin("input3").getValue()).isEqualTo("");
        assertThat(component.getHistory().values()).isEmpty();

        // ---------------------------------------
        // Test #1
        // ---------------------------------------
        component.getInputPin("input1").setValue("foo");
        component.getInputPin("input2").setValue("bar");
        component.getInputPin("input3").setValue("baz");
        component.execute();

        // ---------------------------------------
        // Verification #1
        // ---------------------------------------
        assertThat(component.getHistory().values()).containsExactly("[foo, bar, baz]");

        // ---------------------------------------
        // Test #2 and #3
        // ---------------------------------------
        component.getInputPin("input1").setValue("1");
        component.getInputPin("input2").setValue("2");
        component.getInputPin("input3").setValue("3");
        component.execute();
        component.getInputPin("input1").setValue("A");
        component.getInputPin("input2").setValue("B");
        component.getInputPin("input3").setValue("C");
        component.execute();

        // ---------------------------------------
        // Verification for #2, #3 and #4
        // ---------------------------------------
        assertThat(component.getHistory().values()).containsExactly(
                "[foo, bar, baz]", //
                "[1, 2, 3]", //
                "[A, B, C]" //
        );
        assertThat(component.getData()).isEqualTo("[A, B, C]");
    }

    /**
     * Test Input Component for this test suite only
     */
    private static class TestOutboxComponent extends AbstractOutboxComponent {
        private TestOutboxComponent() {
            super(mock(EventKey.class), new TestOutbox());
        }
    }

    private static class TestOutbox implements Outbox {
        @Input
        private String input1, input2, input3;

        @Override
        public String getData() {
            return "[" + input1 + ", " + input2 + ", " + input3 + "]";
        }
    }
}
