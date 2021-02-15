package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link AbstractInboxComponent}
 */
class AbstractInboxComponentTest {

    @Test
    @DisplayName("Test Abstract Inbox Component")
    void testComponent() {
        final var component = new TestInboxComponent();

        assertThat(component.getUid()).isNotNull();
        assertThat(component.getOutputConnectors()).hasSize(2);
        assertThat(component.getOutputPins()).hasSize(2);
        assertThat(component.getOutputPin("str").getValue()).isEqualTo("");
        assertThat(component.getOutputPin("str2").getValue()).isEqualTo("");
        assertThat(component.getHistory().values()).isEmpty();

        // ---------------------------------------
        // Test #1
        // ---------------------------------------
        component.onNext("foobar");

        // ---------------------------------------
        // Verification #1
        // ---------------------------------------
        assertThat(component.getOutputPin("str").getValue()).isEqualTo("FOOBAR");
        assertThat(component.getOutputPin("str2").getValue()).isEqualTo("raboof");
        assertThat(component.getHistory().values()).containsExactly("foobar");

        // ---------------------------------------
        // Test #2
        // ---------------------------------------
        component.onNext("barbaz");

        // ---------------------------------------
        // Verification #2
        // ---------------------------------------
        assertThat(component.getOutputPin("str").getValue()).isEqualTo("BARBAZ");
        assertThat(component.getOutputPin("str2").getValue()).isEqualTo("zabrab");
        assertThat(component.getHistory().values()).containsExactly("foobar", "barbaz");

        // ---------------------------------------
        // Test #3, #4 and #5
        // ---------------------------------------
        component.onNext("lorem");
        component.onNext("ipsum");
        component.onNext("dolor");

        // ---------------------------------------
        // Verification for #3, #4 and #5
        // ---------------------------------------
        assertThat(component.getOutputPin("str").getValue()).isEqualTo("DOLOR");
        assertThat(component.getOutputPin("str2").getValue()).isEqualTo("rolod");
        assertThat(component.getHistory().values()).containsExactly("foobar", "barbaz", "lorem", "ipsum", "dolor");
    }

    /**
     * Test Input Component for this test suite only
     */
    private static class TestInboxComponent extends AbstractInboxComponent {
        private TestInboxComponent() {
            super(mock(EventKey.class), new TestInbox());
        }
    }

    private static class TestInbox implements Inbox {
        @Output
        private String str;

        @Output
        private String str2;

        @Override
        public void accept(final Object data) {
            final var dataAsString = (String) data;
            str = dataAsString.toUpperCase();
            str2 = new StringBuffer(dataAsString).reverse().toString();
        }
    }
}
