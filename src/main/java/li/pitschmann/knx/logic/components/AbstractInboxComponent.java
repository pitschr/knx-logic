package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.History;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorFactory;
import li.pitschmann.knx.logic.event.EventKey;

import java.util.List;
import java.util.Objects;

/**
 * Abstract implementation for wrapped {@link Inbox} object and contains
 * basic implementations for {@link InboxComponent}.
 *
 * @author PITSCHR
 */
abstract class AbstractInboxComponent extends AbstractComponent<Inbox> implements InboxComponent {
    private final History<Object> history = new History<>();
    private final List<Connector> outputConnectors;
    private final EventKey eventKey;

    /**
     * Package-protected constructor that wraps the {@link Inbox} object
     *
     * @param eventKey for invocation by an event; may not be null
     * @param inbox    the {@link Inbox} instance to be wrapped; may not be null
     */
    AbstractInboxComponent(final EventKey eventKey, final Inbox inbox) {
        super(inbox);
        this.eventKey = Objects.requireNonNull(eventKey);
        outputConnectors = ConnectorFactory.getOutputConnectors(inbox);
    }

    @Override
    public void onNext(final Object data) {
        history.addHistoryEntry(Objects.requireNonNull(data));
        getWrappedObject().accept(data);
    }

    @Override
    public EventKey getEventKey() {
        return eventKey;
    }

    @Override
    public History<Object> getHistory() {
        return history;
    }

    @Override
    public List<Connector> getConnectors() {
        return getOutputConnectors();
    }

    @Override
    public List<Connector> getOutputConnectors() {
        return outputConnectors;
    }
}
