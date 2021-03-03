package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static test.TestHelpers.createLogicComponent;

/**
 * Test {@link StaticConnector}
 */
class StaticConnectorTest {

    @Test
    @DisplayName("Test instantiation of a StaticConnector from component")
    void connectorFromComponent() {
        final var logicComponent = createLogicComponent(
                new Logic() {
                    @Input
                    private boolean i;
                    @Output
                    private boolean o;
                    @Override
                    public void logic() {
                        // NO-OP (not a part of test)
                    }
                }
        );

        assertThat(logicComponent.getConnectors()).hasSize(2); // input + output connectors
        assertThat(logicComponent.getPins()).hasSize(2);

        assertThat(logicComponent.getInputConnectors()).hasSize(1);
        assertThat(logicComponent.getInputPins()).hasSize(1);
        assertPinValue(logicComponent.getInputConnector("i"), false);

        assertThat(logicComponent.getOutputConnectors()).hasSize(1);
        assertThat(logicComponent.getOutputPins()).hasSize(1);
        assertPinValue(logicComponent.getOutputConnector("o"), false);

        // detailed check of single connector
        final var connector = (StaticConnector) logicComponent.getInputConnector("i");
        final var pinFromStream = connector.getPinStream().collect(Collectors.toList());
        assertThat(pinFromStream).hasSize(1);
        final var pin = connector.getPin();
        assertThat(pin).isSameAs(pinFromStream.get(0));
        assertThat(pin.getConnector()).isSameAs(connector);
        assertThat(pin.getDescriptor()).isSameAs(connector.getDescriptor());
        assertThat(pin.getValue()).isEqualTo(false); // default instantiated value

        assertThat(connector).hasToString(
                String.format("StaticConnector{fieldName=i, fieldType=java.lang.Boolean, pin=%s}", pin.getUid())
        );
    }

    @Test
    @DisplayName("Test Static Connector with and without initial value")
    void connectorInitialValues() {
        final var logicComponent = createLogicComponent(
                new Logic() {
                    @Input // index: 0
                    private int a;
                    @Input // index: 1
                    private final Integer b = null;
                    @Input // index: 2+3
                    private int c, d;
                    @Input // index: 4+5
                    private Integer e, f;
                    @Input // index: 6
                    private final int g = 13;
                    @Input // index: 7
                    private final Integer h = Integer.valueOf(17);
                    @Input // index: 8+9
                    private final int i = 19;
                    @Input // index: 8+9
                    private final int j = 23;
                    @Input // index: 10+11
                    private final int k = Integer.valueOf(27);
                    @Input // index: 10+11
                    private final int l = Integer.valueOf(31);
                    @Output
                    private final String m = "Hello";

                    @Override
                    public void logic() {
                        // NO-OP
                    }
                }
        );

        assertThat(logicComponent.getConnectors()).hasSize(13); // input + output connectors
        assertThat(logicComponent.getPins()).hasSize(13);

        assertThat(logicComponent.getInputConnectors()).hasSize(12);
        assertThat(logicComponent.getInputPins()).hasSize(12);
        assertPinValue(logicComponent.getInputConnector("a"), 0);
        assertPinValue(logicComponent.getInputConnector("b"), 0);
        assertPinValue(logicComponent.getInputConnector("c"), 0);
        assertPinValue(logicComponent.getInputConnector("d"), 0);
        assertPinValue(logicComponent.getInputConnector("e"), 0);
        assertPinValue(logicComponent.getInputConnector("f"), 0);
        assertPinValue(logicComponent.getInputConnector("g"), 13);
        assertPinValue(logicComponent.getInputConnector("h"), 17);
        assertPinValue(logicComponent.getInputConnector("i"), 19);
        assertPinValue(logicComponent.getInputConnector("j"), 23);
        assertPinValue(logicComponent.getInputConnector("k"), 27);
        assertPinValue(logicComponent.getInputConnector("l"), 31);

        assertThat(logicComponent.getOutputConnectors()).hasSize(1);
        assertThat(logicComponent.getOutputPins()).hasSize(1);
        assertPinValue(logicComponent.getOutputConnector("m"), "Hello");
    }

    /**
     * Asserts the value of static pin that is hold by {@link StaticConnector}.
     *
     * @param connector     connector that holds a static pin
     * @param expectedValue expected value to be compared
     */
    private void assertPinValue(final Connector connector, final Object expectedValue) {
        assertThat(connector).isInstanceOf(StaticConnector.class);
        assertThat(((StaticConnector) connector).getPin().getValue()).isEqualTo(expectedValue);
    }


}
