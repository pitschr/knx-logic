package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.HistoryAware;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.event.Event;
import li.pitschmann.knx.logic.event.EventKey;

/**
 * Generic Outbox Component
 *
 * @author PITSCHR
 */
public interface OutboxComponent extends
        Component,
        InputConnectorAware,
        HistoryAware<Object> {

    /**
     * Invoke execution for outbox component and
     * return the current data from {@link #getData()}
     * and adds to the history.
     *
     * @return data from outbox; may not be null
     */
    Object execute();

    /**
     * Returns the current data of {@link OutboxComponent}
     *
     * @return data from outbox; may not be null
     */
    Object getData();

    /**
     * The {@link EventKey} that is observed by the receiver
     * of the {@link Event} and contains information for further
     * workflow.
     *
     * @return the {@link EventKey}; may not be null
     */
    EventKey getEventKey();
}
