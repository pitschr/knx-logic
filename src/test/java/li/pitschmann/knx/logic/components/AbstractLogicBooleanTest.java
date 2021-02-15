package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.Logic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createLogicComponent;

/**
 * Abstract class for:
 * <ul>
 * <li>{@link AndLogicBooleanListConstructorTest}</li>
 * <li>{@link AndLogicBooleanListFieldTest}</li>
 * <li>{@link AndLogicBooleanObjectTest}</li>
 * <li>{@link AndLogicBooleanPrimitiveTest}</li>
 * </ul>
 * <p>
 * All should have the same behavior depending on different initialization possibilities.
 */
// @formatter:off
public abstract class AbstractLogicBooleanTest<T extends Logic> {

    /**
     * The class of Logic Component that should be instantiated
     *
     * @return class of Logic Component
     */
    protected abstract Class<T> logicClass();

    @Test
    @DisplayName("Test Logic in initialization state")
    public void testInitialization() {
        final var logic = createLogicComponent(logicClass());
        assertThat(logic.getInputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getInputPin(1).getValue()).isEqualTo(true);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
    }

    @Test
    @DisplayName("Test refreshed status in case of multiple executions")
    public void testRefreshedStatus() {
        final var logic = createLogicComponent(logicClass());

        final var input0 = logic.getInputPin(0);
        final var input1 = logic.getInputPin(1);
        final var output = logic.getOutputPin(0);

        // Execution #1: [true, false] => false (init)
        input0.setValue(true);
        input1.setValue(false);
        assertThat(input0.isRefresh()).isTrue(); // refreshed because of initialization
        assertThat(input1.isRefresh()).isTrue(); // refreshed because of initialization
        assertThat(output.isRefresh()).isTrue(); // refreshed because of initialization
        logic.execute();
        assertThat(input0.isRefresh()).isFalse(); // refresh flag reset for input after execution
        assertThat(input1.isRefresh()).isFalse(); // refresh flag reset for input after execution
        assertThat(output.isRefresh()).isTrue();  // output because of initialization

        // Execution #2, 3, 4: [true, false] => false (re-set with same value)
        input0.setValue(true);
        input1.setValue(false);
        assertThat(input0.isRefresh()).isFalse(); // not refreshed, because of no value change
        assertThat(input1.isRefresh()).isFalse(); // not refreshed, because of no value change
        assertThat(output.isRefresh()).isTrue();  // still marked as 'refreshed' because of Execution #1 (will be reset when logic is re-executed)
        logic.execute();
        assertThat(input0.isRefresh()).isFalse();
        assertThat(input1.isRefresh()).isFalse();
        assertThat(output.isRefresh()).isFalse(); // not refreshed, because of no value change
        for (var i = 0; i < 2; i++) {
            input0.setValue(true);
            input1.setValue(false);
            assertThat(input0.isRefresh()).isFalse(); // not refreshed, because of no value change
            assertThat(input1.isRefresh()).isFalse(); // not refreshed, because of no value change
            assertThat(output.isRefresh()).isFalse(); // not refreshed, because of no value change
            logic.execute();
            assertThat(input0.isRefresh()).isFalse();
            assertThat(input1.isRefresh()).isFalse();
            assertThat(output.isRefresh()).isFalse();
        }

        // Execution #5: [true, true] => true (set: 2nd input)
        input1.setValue(true);
        assertThat(input0.isRefresh()).isFalse();
        assertThat(input1.isRefresh()).isTrue();
        assertThat(output.isRefresh()).isFalse();
        logic.execute();
        assertThat(input0.isRefresh()).isFalse();
        assertThat(input1.isRefresh()).isFalse();  // refresh flag cleared after logic execution
        assertThat(output.isRefresh()).isTrue();   // value changed (from false -> true)

        // Execution #6: [false, true] => false (set: 1st input)
        input0.setValue(false);
        assertThat(input0.isRefresh()).isTrue();
        assertThat(input1.isRefresh()).isFalse();
        assertThat(output.isRefresh()).isTrue();   // still true because logic is not executed yet
        logic.execute();
        assertThat(input0.isRefresh()).isFalse();
        assertThat(input1.isRefresh()).isFalse();
        assertThat(output.isRefresh()).isTrue();   // value changed (from true -> false)

        // Execution #7: [false, false] => false (set: 2nd input)
        input1.setValue(false);
        assertThat(input0.isRefresh()).isFalse();
        assertThat(input1.isRefresh()).isTrue();
        assertThat(output.isRefresh()).isTrue();   // still true because logic is not executed yet
        logic.execute();
        assertThat(input0.isRefresh()).isFalse();  // refresh flag cleared after logic execution
        assertThat(input1.isRefresh()).isFalse();
        assertThat(output.isRefresh()).isFalse();  // not refreshed, because of no value change

        // Assert how much times the logic component was executed
        assertThat(logic.executedCount()).isEqualTo(7);

        // Assert how much times the pin has been refreshed
        // (init + #1) false -> (#6) true
        assertThat(input0.refreshCount()).isEqualTo(2);
        // (init + #1) false -> (#5) true -> (#7) false
        assertThat(input1.refreshCount()).isEqualTo(3);
        // (#1) false -> (#5) true -> (#6) -> false
        assertThat(output.refreshCount()).isEqualTo(3);
    }

    @Test
    @DisplayName("Test: 'true' and 'true'")
    public void testPrimitiveTrueAndTrue() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(true);
        logic.getInputPin(1).setValue(true);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(true);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.TRUE);
    }

    @Test
    @DisplayName("Test: 'Boolean.TRUE' and 'Boolean.TRUE'")
    public void testObjectTrueAndTrue() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(Boolean.TRUE);
        logic.getInputPin(1).setValue(Boolean.TRUE);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(true);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.TRUE);
    }

    @Test
    @DisplayName("Test: 'true' and 'false'")
    public void testPrimitiveTrueAndFalse() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(true);
        logic.getInputPin(1).setValue(false);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("Test: 'Boolean.TRUE' and 'Boolean.FALSE'")
    public void testObjectTrueAndFalse() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(Boolean.TRUE);
        logic.getInputPin(1).setValue(Boolean.FALSE);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("Test: 'false' and 'false'")
    public void testPrimitiveFalseAndFalse() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(false);
        logic.getInputPin(1).setValue(false);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.FALSE);
    }

    @Test
    @DisplayName("Test: 'Boolean.FALSE' and 'Boolean.FALSE'")
    public void testObjectFalseAndFalse() {
        final var logic = createLogicComponent(logicClass());

        logic.getInputPin(0).setValue(Boolean.FALSE);
        logic.getInputPin(1).setValue(Boolean.FALSE);

        logic.execute();

        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin(0).getValue()).isEqualTo(Boolean.FALSE);
    }
}
