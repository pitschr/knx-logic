package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.History;
import li.pitschmann.knx.logic.components.outbox.Outbox;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorFactory;
import li.pitschmann.knx.logic.event.EventKey;

import java.util.List;
import java.util.Objects;

/**
 * Abstract implementation that contains most basic implementations
 * for {@link OutboxComponent}
 *
 * @author PITSCHR
 */
abstract class AbstractOutboxComponent extends AbstractComponent<Outbox> implements OutboxComponent {
    private final History<Object> history = new History<>();
    private final List<Connector> inputConnectors;
    private final EventKey eventKey;

    /**
     * Package-protected constructor that wraps the {@link Outbox} object
     *
     * @param eventKey for invocation by an event; may not be null
     * @param outbox   the {@link Outbox} instance to be wrapped; may not be null
     */
    AbstractOutboxComponent(final EventKey eventKey, final Outbox outbox) {
        super(outbox);
        this.eventKey = Objects.requireNonNull(eventKey);
        inputConnectors = ConnectorFactory.getInputConnectors(outbox);
    }

    @Override
    public Object execute() {
        final var data = getData();
        history.addHistoryEntry(data);
        return data;
    }

    @Override
    public Object getData() {
        return Objects.requireNonNull(getWrappedObject().getData());
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
        return getInputConnectors();
    }

    @Override
    public List<Connector> getInputConnectors() {
        return inputConnectors;
    }
}
