package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.uid.UID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link AbstractComponent}
 */
class AbstractComponentTest {

    @Test
    @DisplayName("OK: AbstractComponent with execution")
    void testSuccessful() {
        final var logic = mock(Logic.class);
        final var component = new TestComponent(logic);

        assertThat(component.getUid()).isNotNull();
        assertThat(component.getWrappedObject()).isSameAs(logic);
    }

    @Test
    @DisplayName("Test the override of UID")
    void testPinUidOverride() {
        final var component = new TestComponent(mock(Object.class));
        final var uidMock = mock(UID.class);

        // set UID
        component.setUid(uidMock);

        // verify
        assertThat(component.getUid()).isSameAs(uidMock);
    }

    /**
     * Implementation of AbstractComponent for testing purposes
     */
    private static class TestComponent extends AbstractComponent<Object> {
        private TestComponent(final Object obj) {
            super(obj);
        }
    }

}
