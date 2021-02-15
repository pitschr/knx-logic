package experimental.api;

import experimental.api.v1.strategies.CreateStrategy;
import experimental.api.v1.strategies.KnxInboxCreateStrategy;
import experimental.api.v1.strategies.KnxOutboxCreateStrategy;
import experimental.api.v1.strategies.LogicCreateStrategy;
import experimental.api.v1.strategies.VariableInboxCreateStrategy;
import experimental.api.v1.strategies.VariableOutboxCreateStrategy;
import li.pitschmann.knx.logic.LogicRepository;
import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.OutboxComponent;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Factory for creating component using strategies
 *
 * @author PITSCHR
 */
public final class ComponentFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentFactory.class);
    private final KnxInboxCreateStrategy knxInboxCreateStrategy = new KnxInboxCreateStrategy();
    private final KnxOutboxCreateStrategy knxOutboxCreateStrategy = new KnxOutboxCreateStrategy();
    private final VariableInboxCreateStrategy varInboxCreateStrategy = new VariableInboxCreateStrategy();
    private final VariableOutboxCreateStrategy varOutboxCreateStrategy = new VariableOutboxCreateStrategy();
    private final LogicRepository logicRepository = new LogicRepository();
    private final LogicCreateStrategy logicCreateStrategy = new LogicCreateStrategy(logicRepository);

    /**
     * Returns the {@link LogicRepository}
     *
     * @return actual {@link LogicRepository}
     */
    public LogicRepository getLogicRepository() {
        return logicRepository;
    }

    /**
     * Creates a new {@link InboxComponent} for given event and map of data that
     * may contain relevant information for creation of {@link InboxComponent}
     *
     * @param eventType the type of vent to select the right {@link CreateStrategy}
     * @param data      map of data containing information for {@link CreateStrategy}
     * @return new {@link InboxComponent}
     * @throws IllegalArgumentException if unsupported eventType is provided
     */
    public InboxComponent createInbox(final String eventType, final Map<String, String> data) {
        LOG.debug("Create inbox component for event '{}': {}", eventType, data);
        if (KnxEventChannel.CHANNEL_ID.equalsIgnoreCase(eventType)) {
            return knxInboxCreateStrategy.apply(data);
        } else if (VariableEventChannel.CHANNEL_ID.equalsIgnoreCase(eventType)) {
            return varInboxCreateStrategy.apply(data);
        }
        throw new IllegalArgumentException("No suitable inbox component strategy found for event type: " + eventType);
    }

    /**
     * Creates a new {@link OutboxComponent} for given event and map of data that
     * may contain relevant information for creation of {@link OutboxComponent}
     *
     * @param eventType the type of vent to select the right {@link CreateStrategy}
     * @param data      map of data containing information for {@link CreateStrategy}
     * @return new {@link OutboxComponent}
     * @throws IllegalArgumentException if unsupported eventType is provided
     */
    public OutboxComponent createOutbox(final String eventType, final Map<String, String> data) {
        LOG.debug("Create outbox component for event '{}': {}", eventType, data);
        if (KnxEventChannel.CHANNEL_ID.equalsIgnoreCase(eventType)) {
            return knxOutboxCreateStrategy.apply(data);
        } else if (VariableEventChannel.CHANNEL_ID.equalsIgnoreCase(eventType)) {
            return varOutboxCreateStrategy.apply(data);
        }
        throw new IllegalArgumentException("No suitable outbox component strategy found for event type: " + eventType);
    }

    /**
     * Creates a new {@link LogicComponent} based on map of data
     *
     * @param data map of data containing information for {@link CreateStrategy}
     * @return new {@link LogicComponent}
     */
    public LogicComponent createLogic(final Map<String, String> data) {
        LOG.debug("Create logic component for: {}", data);
        return logicCreateStrategy.apply(data);
    }
}
