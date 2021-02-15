package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static test.TestHelpers.createLogicComponent;

/**
 * Test Suite for {@link ConnectorAware}
 */
class ConnectorAwareTest {

    @Test
    @DisplayName("Get connector by name")
    void testGetConnectorByName() {
        final var component = createLogicComponent(
                new Logic() {
                    @Input
                    private boolean boolInputValue;

                    @Input
                    private Integer intInputValue;

                    @Input
                    private Iterable<Object> objects;

                    @Output
                    private List<String> stringList;

                    @Override
                    public void logic() {
                        // NO-OP
                    }
                }
        );

        assertThat(component.getConnectors()).hasSize(4);
        assertThat(component.getInputConnectors()).hasSize(3);
        assertThat(component.getOutputConnectors()).hasSize(1);

        // input connector exists
        final var intInputValueConnector = component.getInputConnector(1);
        assertThat(component.getConnector("intInputValue")).isSameAs(intInputValueConnector);
        assertThat(component.getInputConnector("intInputValue")).isSameAs(intInputValueConnector);
        assertThatThrownBy(() -> component.getOutputConnector("intInputValue"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Connector with field name found: intInputValue");

        // output connector exists
        final var stringListConnector = component.getOutputConnector(0);
        assertThat(component.getConnector("stringList")).isSameAs(stringListConnector);
        assertThatThrownBy(() -> component.getInputConnector("stringList"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Connector with field name found: stringList");
        assertThat(component.getOutputConnector("stringList")).isSameAs(stringListConnector);

        // no suitable connector exists
        assertThatThrownBy(() -> component.getConnector("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Connector with field name found: foobar");
        assertThatThrownBy(() -> component.getInputConnector("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Input Connector with field name found: foobar");
        assertThatThrownBy(() -> component.getOutputConnector("foobar"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No Output Connector with field name found: foobar");
    }
}
