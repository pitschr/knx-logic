package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.exceptions.LoaderException;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.StaticPin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link LogicComponentImpl}
 */
class LogicComponentImplTest {

    @Test
    @DisplayName("Test Logic Component without Input and Outputs")
    void testEmptyLogic() {
        final Logic logic = () -> {
        };
        final var component = new LogicComponentImpl(logic);

        assertThat(component.getInputConnectors()).isEmpty();
        assertThat(component.getInputPins()).isEmpty();
        assertThat(component.getOutputConnectors()).isEmpty();
        assertThat(component.getOutputPins()).isEmpty();

        assertThatThrownBy(() -> component.getInputConnector(0)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> component.getOutputConnector(0)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> component.getInputPin(0)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        assertThatThrownBy(() -> component.getOutputPin(0)).isInstanceOf(ArrayIndexOutOfBoundsException.class);

        assertThat(component.executedCount()).isZero();
        assertThat(component.logicCount()).isZero();
        component.execute(); // 1st run (init)
        component.execute(); // 2nd run (no change)
        component.execute(); // 3rd run (no change)
        assertThat(component.executedCount()).isEqualTo(3);
        assertThat(component.logicCount()).isEqualTo(1);

        assertThat(component).hasToString( //
                String.format("LogicComponentImpl{" + //
                                "uid=%s, " + //
                                "logicClass=%s, " + //
                                "executedCount=3, " + //
                                "executedTime=%s, " + //
                                "logicCount=1, " + //
                                "inputConnectors=0, " + //
                                "outputConnectors=0" + //
                                "}", //
                        component.getUid(), //
                        component.getWrappedObject().getClass().getName(), //
                        component.executedTime()
                )
        );
    }

    @Test
    @DisplayName("Test Logic Component with Input and Outputs")
    void testLogicWithInputAndOutputs() {
        final var logic = new Logic() {
            @Input(min = 3)
            private List<Integer> ints;

            @Output
            private Integer sum;

            @Output
            private Integer max;

            @Override
            public void logic() {
                this.sum = ints.stream().mapToInt(Integer::valueOf).sum();
                this.max = ints.stream().mapToInt(Integer::valueOf).max().orElse(0);
            }
        };
        final var component = new LogicComponentImpl(logic);

        assertThat(component.getInputConnectors()).hasSize(1);
        assertThat(component.getInputConnector("ints")).isInstanceOf(DynamicConnector.class);
        assertThat(component.getInputPins()).hasSize(3);
        assertThat(component.getInputPin("ints[0]")).isInstanceOf(DynamicPin.class);
        assertThat(component.getInputPin("ints[1]")).isInstanceOf(DynamicPin.class);
        assertThat(component.getInputPin("ints[2]")).isInstanceOf(DynamicPin.class);

        assertThat(component.getOutputConnectors()).hasSize(2);
        assertThat(component.getOutputConnector("sum")).isInstanceOf(StaticConnector.class);
        assertThat(component.getOutputConnector("max")).isInstanceOf(StaticConnector.class);
        assertThat(component.getOutputPins()).hasSize(2);
        assertThat(component.getOutputPin("sum")).isInstanceOf(StaticPin.class);
        assertThat(component.getOutputPin("max")).isInstanceOf(StaticPin.class);

        // not executed yet
        assertThat(component.executedCount()).isZero();
        assertThat(component.logicCount()).isZero();
        assertThat(component).hasToString( //
                String.format("LogicComponentImpl{" + //
                                "uid=%s, " + //
                                "logicClass=%s, " + //
                                "executedCount=0, " + //
                                "executedTime=0, " + //
                                "logicCount=0, " + //
                                "inputConnectors=1, " + //
                                "outputConnectors=2" + //
                                "}", //
                        component.getUid(), //
                        component.getWrappedObject().getClass().getName() //
                )
        );

        // execute it for the first time (init = 1st run)
        component.getInputPin("ints[0]").setValue(13);
        component.getInputPin("ints[1]").setValue(31);
        component.getInputPin("ints[2]").setValue(11);
        component.execute();

        // execute 2nd and 3rd time
        component.getInputPin("ints[0]").setValue(17);
        component.execute(); // 2nd run (change 13 -> 17)
        component.execute(); // 3rd run (no change)

        assertThat(component.executedCount()).isEqualTo(3);
        assertThat(component.logicCount()).isEqualTo(2);

        assertThat(component.getOutputPin("sum").getValue()).isEqualTo(59);
        assertThat(component.getOutputPin("max").getValue()).isEqualTo(31);

        assertThat(component).hasToString( //
                String.format("LogicComponentImpl{" + //
                                "uid=%s, " + //
                                "logicClass=%s, " + //
                                "executedCount=3, " + //
                                "executedTime=%s, " + //
                                "logicCount=2, " + //
                                "inputConnectors=1, " + //
                                "outputConnectors=2" + //
                                "}", //
                        component.getUid(), //
                        component.getWrappedObject().getClass().getName(), //
                        component.executedTime()
                )
        );
    }

//    @Test
//    @DisplayName("Try to load a logic component from class multiple times")
//    void loadLogicClassMultipleTimes() {
//        // First load
//        final var logic = new LogicComponentImpl(LogicA.class);
//
//        // Second load
//        final var logic2 = new LogicComponentImpl(LogicA.class);
//
//        assertThat(logic).isNotSameAs(logic2);
//        assertThat(logic.getWrappedObject()).isNotSameAs(logic2.getWrappedObject());
//    }
//
//    @Test
//    @DisplayName("Try to load logic component without no-arg constructor")
//    void loadLogicClassWithoutNoArg() {
//        assertThatThrownBy(() -> new LogicComponentImpl(LogicNoArg.class))
//                .isInstanceOf(LoaderException.class)
//                .hasMessage("Could not load logic class: class li.pitschmann.knx.logic.components.LogicComponentImplTest$LogicNoArg");
//    }

    private static class LogicNoArg implements Logic {
        private LogicNoArg(final boolean unused) {
            // NO-OP
        }

        @Override
        public void logic() {
            // NO-OP
        }
    }
}
