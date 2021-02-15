package experimental.api.v1.strategies;

import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.VariableInbox;
import li.pitschmann.knx.logic.event.VariableEventChannel;

import java.util.Map;

/**
 * {@link CreateStrategy} implementation for VARIABLE {@link InboxComponent}
 *
 * @author PITSCHR
 */
public class VariableInboxCreateStrategy implements CreateStrategy<InboxComponent> {
    @Override
    public InboxComponent apply(final Map<String, String> data) {
        return new InboxComponentImpl(
                VariableEventChannel.createKey(data.get("name")), //
                new VariableInbox()                        //
        );
    }
}
