package li.pitschmann.knx.logic;

import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.event.Event;

import java.util.Objects;
import java.util.Set;

/**
 * The context for routing and should be used by
 * {@link Router} and {@link RouterInternal} exclusively.
 * <p>
 * This class is immutable
 *
 * @author PITSCHR
 */
final class RouterContext {
    private final Router router;
    private final Set<InboxComponent> inboxComponents;
    private final Event event;

    /**
     * Creates a new context for router
     *
     * @param router          the {@link Router} instance; may not be null
     * @param inboxComponents a set of {@link InboxComponent} that is subject to be triggered by {@link Router}; may not be null
     * @param event           the event instance ; may not be null
     */
    RouterContext(final Router router, final Set<InboxComponent> inboxComponents, final Event event) {
        this.router = Objects.requireNonNull(router);
        this.inboxComponents = Set.copyOf(inboxComponents);
        this.event = Objects.requireNonNull(event);
    }

    /**
     * Instance of Router for outbound context
     *
     * @return instance of {@link Router}
     */
    public Router getRouter() {
        return router;
    }

    /**
     * Returns set of {@link InboxComponent}
     *
     * @return immutable set
     */
    public Set<InboxComponent> getInboxComponents() {
        return inboxComponents;
    }

    /**
     * Returns the event
     *
     * @return current event
     */
    public Event getEvent() {
        return event;
    }
}
