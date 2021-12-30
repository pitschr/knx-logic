package li.pitschmann.knx.api.v1.strategies;

import li.pitschmann.knx.logic.components.OutboxComponent;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.components.outbox.VariableOutbox;
import li.pitschmann.knx.logic.event.VariableEventChannel;

import java.util.Map;

/**
 * {@link CreateStrategy} implementation for VARIABLE {@link OutboxComponent}
 *
 * @author PITSCHR
 */
public class VariableOutboxCreateStrategy implements CreateStrategy<OutboxComponent> {
    @Override
    public OutboxComponent apply(final Map<String, String> data) {
        return new OutboxComponentImpl(
                VariableEventChannel.createKey(data.get("name")), //
                new VariableOutbox()                       //
        );
    }
}
