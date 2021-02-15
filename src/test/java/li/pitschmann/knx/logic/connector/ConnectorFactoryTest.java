package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link ConnectorFactory}
 */
public class ConnectorFactoryTest {

    @Test
    @DisplayName("Try create connectors from component containing no connectors")
    public void noConnectors() {
        // no inputs, no outputs
        final Logic component = () -> {
        };

        assertThat(ConnectorFactory.getInputConnectors(component)).isEmpty();
    }

    @Test
    @DisplayName("Create one Input Connector")
    public void inputConnectors() {
        // one input, no outputs
        final var component = new Logic() {
            @Input
            private boolean i;

            @Override
            public void logic() {
                // NO-OP (not a part of test)
            }
        };

        final var inputConnectors = ConnectorFactory.getInputConnectors(component);
        assertThat(inputConnectors).hasSize(1);
        assertThat(inputConnectors.get(0)).isInstanceOf(StaticConnector.class);
    }

    @Test
    @DisplayName("Create two Output Connectors")
    public void outputConnectors() {
        // two inputs, two outputs
        final var component = new Logic() {
            @Output
            private boolean output;

            @Output
            private List<Boolean> outputs;

            @Override
            public void logic() {
                // NO-OP (not a part of test)
            }
        };

        final var outputConnectors = ConnectorFactory.getOutputConnectors(component);
        assertThat(outputConnectors).hasSize(2);
        assertThat(outputConnectors.get(0)).isInstanceOf(StaticConnector.class);
        assertThat(outputConnectors.get(1)).isInstanceOf(DynamicConnector.class);
    }

    @Test
    @DisplayName("Constructor not instantiable")
    public void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = ConnectorFactory.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }
}
