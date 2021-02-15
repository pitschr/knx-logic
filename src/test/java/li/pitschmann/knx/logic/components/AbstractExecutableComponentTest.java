package li.pitschmann.knx.logic.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link AbstractExecutableComponent}
 */
public class AbstractExecutableComponentTest {

    @Test
    @DisplayName("OK: AbstractExecutableComponent")
    public void testSuccessful() {
        final var logic = new LogicA();
        final var component = new AbstractExecutableComponent<>(logic) {
            @Override
            protected void executeSafe() {
                // NO-OP
            }
        };

        assertThat(component.getUid()).isNotNull();
        assertThat(component.getWrappedObject()).isSameAs(logic);

        // not executed yet
        assertThat(component.executedCount()).isZero();
        assertThat(component.executedTime()).isZero();

        // execute it once time and then verify
        component.execute();
        assertThat(component.executedCount()).isEqualTo(1);
        assertThat(component.executedTime()).isNotZero();
    }

    @Test
    @DisplayName("ERROR: AbstractExecutableComponent with issue in execution")
    public void testFailure() {
        final var logic = new LogicA();
        final var component = new AbstractExecutableComponent<>(logic) {
            @Override
            protected void executeSafe() {
                throw new RuntimeException("Boo!");
            }
        };

        // not executed yet
        assertThat(component.executedCount()).isZero();
        assertThat(component.executedTime()).isZero();

        // execute it once time and then verify - this one is throwing an exception
        assertThatThrownBy(component::execute).isInstanceOf(RuntimeException.class).hasMessage("Boo!");
        assertThat(component.executedCount()).isEqualTo(1);
        assertThat(component.executedTime()).isNotZero();
    }

}
