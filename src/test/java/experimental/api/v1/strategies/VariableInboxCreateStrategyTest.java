package experimental.api.v1.strategies;

import li.pitschmann.knx.logic.components.inbox.VariableInbox;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link VariableInboxCreateStrategy}
 */
public class VariableInboxCreateStrategyTest {

    @Test
    @DisplayName("Create VARIABLE Inbox Component")
    public void createVariableInboxComponent() {
        final var data = Map.of("name", "foobar");

        final var strategy = new VariableInboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("foobar");

        // the wrapped component is a VARIABLE Inbox which contains one output connector
        final var wrappedComponent = component.getOutputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(VariableInbox.class);
    }

}
