package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.HistoryAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;
import li.pitschmann.knx.logic.event.EventKey;

/**
 * Generic Inbox Component
 *
 * @author PITSCHR
 */
public interface InboxComponent extends
        Component,
        OutputConnectorAware,
        HistoryAware<Object> {

    /**
     * Returns the {@link EventKey} that is listened by {@link InboxComponent}
     * if an event with the given key is triggered, then this {@link InboxComponent}
     * will be triggered
     *
     * @return the {@link EventKey}
     */
    EventKey getEventKey();

    /**
     * Data for inbox component for further processing
     *
     * @param data the data that should be processed; may not be {@code null}
     */
    void onNext(final Object data);
}
