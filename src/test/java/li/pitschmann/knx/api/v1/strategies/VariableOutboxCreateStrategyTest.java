package li.pitschmann.knx.api.v1.strategies;

import li.pitschmann.knx.logic.components.outbox.VariableOutbox;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link VariableOutboxCreateStrategy}
 */
public class VariableOutboxCreateStrategyTest {

    @Test
    @DisplayName("Create VARIABLE Outbox Component")
    public void createVariableOutboxComponent() {
        final var data = Map.of("name", "foobaz");

        final var strategy = new VariableOutboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("foobaz");

        // the wrapped component is a VARIABLE Outbox which contains one output connector
        final var wrappedComponent = component.getInputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(VariableOutbox.class);
    }

}
