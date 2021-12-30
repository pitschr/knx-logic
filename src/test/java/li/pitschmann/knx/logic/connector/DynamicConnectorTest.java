package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.exceptions.MaximumBoundException;
import li.pitschmann.knx.logic.exceptions.MinimumBoundException;
import li.pitschmann.knx.logic.pin.Pin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createLogicComponent;

/**
 * Test {@link DynamicConnector}
 */
class DynamicConnectorTest {

    @Test
    @DisplayName("Test Dynamic Connector with and without initial value")
    void connectorInitialValues() {
        final var logicComponent = createLogicComponent(
                new Logic() {
                    @Input // pin index: 0 (because of default min()=1)
                    private Iterable<Integer> a;
                    @Input // pin index: 1 (because of default min()=1)
                    private Collection<Integer> b;
                    @Input // pin index: 2 (because of default min()=1)
                    private List<Integer> c;
                    @Input // pin index: 3+4 (because there are two values)
                    private final Iterable<String> d = Arrays.asList("foo", "bar");
                    @Input // pin index: 5+6+7 (because there are three values)
                    private final Collection<String> e = Arrays.asList("hello", "world", "!");
                    @Input // pin index: 8+9
                    private final List<String> f = Arrays.asList("lorem", "ipsum");
                    @Input(min = 3) // pin index: 10+11+12 (all three pins should have default initial value)
                    private List<Integer> g;
                    @Input(min = 4) // pin index: 13+14+15+16 (two pins should have default initial value)
                    private final List<Integer> h = Arrays.asList(31, 37);

                    @Override
                    public void logic() {
                        // NO-OP
                    }
                }
                // formatter:on
        );

        assertThat(logicComponent.getConnectors()).hasSize(8);
        assertThat(logicComponent.getPins()).hasSize(17);

        assertThat(logicComponent.getInputConnectors()).hasSize(8);
        assertThat(logicComponent.getInputPins()).hasSize(17);

        assertThat(logicComponent.getOutputConnectors()).isEmpty();
        assertThat(logicComponent.getOutputPins()).isEmpty();

        assertPinValues(logicComponent.getInputConnector("a"), 0);
        assertPinValues(logicComponent.getInputConnector("b"), 0);
        assertPinValues(logicComponent.getInputConnector("c"), 0);
        assertPinValues(logicComponent.getInputConnector("d"), "foo", "bar");
        assertPinValues(logicComponent.getInputConnector("e"), "hello", "world", "!");
        assertPinValues(logicComponent.getInputConnector("f"), "lorem", "ipsum");
        assertPinValues(logicComponent.getInputConnector("g"), 0, 0, 0);
        assertPinValues(logicComponent.getInputConnector("h"), 31, 37, 0, 0);

        final var connector_a = logicComponent.getInputConnector(0);
        assertThat(connector_a)
                .isInstanceOf(DynamicConnector.class)
                .hasToString( //
                        String.format("DynamicConnector{" +  //
                                        "uid=%s, " + //
                                        "fieldName=a, " +    //
                                        "fieldType=java.lang.Integer, " + //
                                        "defaultValue=0, " + //
                                        "pins=%s}",  //
                                connector_a.getUid(), //
                                Arrays.toString(connector_a.getPinStream().map(Pin::getUid).toArray()) //
                        )
                );

        final var connector_e = logicComponent.getInputConnector(4);
        assertThat(connector_e)
                .isInstanceOf(DynamicConnector.class)
                .hasToString( //
                        String.format("DynamicConnector{" + //
                                        "uid=%s, " + //
                                        "fieldName=e, " +    //
                                        "fieldType=java.lang.String, " + //
                                        "defaultValue=, " + // default value is empty string
                                        "pins=%s}",         //
                                connector_e.getUid(), //
                                Arrays.toString(connector_e.getPinStream().map(Pin::getUid).toArray()) //
                        )
                );
    }

    @Test
    @DisplayName("Add new pins to DynamicConnector")
    void addPins() {
        final var component = createLogicComponent(TestLogic.class);

        // get connector
        final var connector = (DynamicConnector) component.getInputConnector(0);  // field: inputs
        assertPins(0, connector);

        // add new pin
        final var pin1 = connector.addPin(); // index: 0
        assertPins(1, connector);
        assertThat(connector.getPin(0)).isSameAs(pin1);

        // add two more pins
        final var pin2 = connector.addPin(); // index: 1
        final var pin3 = connector.addPin(); // index: 2
        assertPins(3, connector);
        assertThat(connector.getPins()).containsExactly(pin1, pin2, pin3);

        // add one pin at index=0
        final var pin4 = connector.addPin(0);
        assertPins(4, connector);
        assertThat(connector.getPins()).containsExactly(pin4, pin1, pin2, pin3);

        // add one pin at index=2
        final var pin5 = connector.addPin(2);
        assertPins(5, connector);
        assertThat(connector.getPins()).containsExactly(pin4, pin1, pin5, pin2, pin3);

        // add more pin (will cause issue because max=5)
        assertThatThrownBy(connector::addPin)
                .isInstanceOf(MaximumBoundException.class)
                .hasMessageStartingWith("Maximum number of pin already reached for connector");
        assertThatThrownBy(() -> connector.addPin(2))
                .isInstanceOf(MaximumBoundException.class)
                .hasMessageStartingWith("Maximum number of pin already reached for connector");
    }

    @Test
    @DisplayName("Try add new pins to DynamicConnector")
    void tryAddPins() {
        final var component = createLogicComponent(TestLogic.class);

        // get connector
        final var connector = (DynamicConnector) component.getInputConnector(0); // field: inputs
        assertPins(0, connector);

        // try to increase to 3 pins
        final var newPins = connector.tryIncrease(3);
        assertPins(3, connector);
        assertThat(newPins).hasSize(3);
        assertThat(connector.getPins()).containsExactly(newPins.get(0), newPins.get(1), newPins.get(2));

        // try to increase to 6 pins (max=5)
        final var newPins2 = connector.tryIncrease(6);
        assertPins(5, connector);
        assertThat(newPins2).hasSize(2); // pin #4, pin #5 (pin #6 is not added due bound maximum limit)
        assertThat(connector.getPins()).containsExactly(
                newPins.get(0), newPins.get(1), newPins.get(2), // from 1st tryIncrease(..)
                newPins2.get(0), newPins2.get(1)                // from 2nd tryIncrease(..)
        );

        // try to "increase" to 4 pins (already has 5 pins)
        final var newPins3 = connector.tryIncrease(4);
        assertThat(newPins3).isEmpty();
        assertPins(5, connector);
    }

    @Test
    @DisplayName("Remove pins from DynamicConnector")
    void removePins() {
        final var component = createLogicComponent(TestLogic.class);

        // get connector
        final var connector = (DynamicConnector) component.getInputConnector(0); // field: inputs
        assertPins(0, connector);

        final var newPins = connector.tryIncrease(5);
        assertPins(5, connector);
        assertThat(newPins).hasSize(5);
        assertThat(connector.getPins()).containsExactly(newPins.get(0), newPins.get(1), newPins.get(2), newPins.get(3), newPins.get(4));

        final var removePin1 = connector.removePin(2); // remove pin at index 2
        assertPins(4, connector);
        assertThat(removePin1).isSameAs(newPins.get(2));
        assertThat(connector.getPins()).containsExactly(newPins.get(0), newPins.get(1), newPins.get(3), newPins.get(4));

        final var removePin2 = connector.removePin(0); // remove pin at index 0
        assertPins(3, connector);
        assertThat(removePin2).isSameAs(newPins.get(0));
        assertThat(connector.getPins()).containsExactly(newPins.get(1), newPins.get(3), newPins.get(4));

        final var removePin3 = connector.removePin(2); // remove pin at index 2
        assertPins(2, connector);
        assertThat(removePin3).isSameAs(newPins.get(4));
        assertThat(connector.getPins()).containsExactly(newPins.get(1), newPins.get(3));

        connector.reset(); // remove remaining pins
        assertPins(0, connector);

        // try to remove one more pin
        assertThatThrownBy(() -> connector.removePin(0))
                .isInstanceOf(MinimumBoundException.class)
            .hasMessageStartingWith("Minimum number of pins already reached for connector");
    }

    @Test
    @DisplayName("Reset DynamicConnector")
    void resetConnector() {
        final var component = createLogicComponent(TestLogic.class);

        // get connector
        final var connector = (DynamicConnector) component.getInputConnector(1); // field: inputs2
        final var existingPins = connector.getPins();
        assertPins(2, connector);

        final var newPins = connector.tryIncrease(5);
        assertPins(5, connector);
        assertThat(newPins).hasSize(3);
        assertThat(connector.getPins()).containsExactly(existingPins.get(0), existingPins.get(1), newPins.get(0), newPins.get(1), newPins.get(2));

        // reset connector
        connector.reset(); // remove remaining pins
        assertPins(2, connector);
        assertThat(connector.getPin(0)).isNotSameAs(existingPins.get(0)); // new initialized pins
        assertThat(connector.getPin(1)).isNotSameAs(existingPins.get(1));
    }

    @Test
    @DisplayName("Test unbound min/max for OutputConnector")
    void testUnboundMinimumAndMaximumForOutputConnector() {
        final var component = createLogicComponent(TestLogic.class);

        // get connector
        final var connector = (DynamicConnector) component.getOutputConnector(0); // field: outputs
        assertPins(1, connector);

        // add pin
        connector.addPin();
        assertPins(2, connector);

        // remove pin
        connector.removePin(0);
        assertPins(1, connector);
    }

    /**
     * Checks if the size of {@code pins} is expected and if all {@code pins} has correct index
     *
     * @param expectedPinSize expected number of pins
     * @param connector       connector to be checked
     */
    private void assertPins(final int expectedPinSize, final DynamicConnector connector) {
        final var pins = connector.getPins();
        final var pinsFromStream = connector.getPinStream().collect(Collectors.toList());

        assertThat(pins).hasSize(expectedPinSize);
        assertThat(pinsFromStream).hasSize(expectedPinSize);
        for (var i = 0; i < pins.size(); i++) {
            assertThat(pins.get(i).getIndex()).isEqualTo(i);
            assertThat(pins.get(i)).isSameAs(pinsFromStream.get(i));
        }
    }

    /**
     * Asserts the values of pins hold by connector with the expected value.
     *
     * @param connector      connector that holds a list of pins
     * @param expectedValues expected values to be compared
     */
    private void assertPinValues(final Connector connector, final Object... expectedValues) {
        assertThat(connector).isInstanceOf(DynamicConnector.class);
        assertThat(connector.getPinStream().map(Pin::getValue).toArray()).containsExactly(expectedValues);
    }

    /**
     * Test Object
     */
    public static class TestLogic implements Logic {
        @Input(min = 0, max = 5)
        private List<String> inputs;

        @Input(min = 2)
        private List<String> inputs2;

        @Output
        private List<String> outputs;

        @Override
        public void logic() {
            // NO-OP
        }
    }

}
