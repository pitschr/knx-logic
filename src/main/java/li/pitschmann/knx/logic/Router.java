package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.core.utils.Sleeper;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.event.Event;
import li.pitschmann.knx.logic.event.EventChannel;
import li.pitschmann.knx.logic.event.EventKey;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the router that is the 'core' of the logic.
 * It consumes {@link Event} by {@link #inbound(Event)} and
 * outgoing {@link Event} are consumed by {@link #outbound(Event)}
 * which calls the suitable {@link EventChannel} for further
 * processing.
 *
 * @author PITSCHR
 */
public final class Router {
    private static final Logger log = LoggerFactory.getLogger(Router.class);
    /**
     * The thread executors for parallel inbound / outbound channel submission
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(8);
    /**
     * <p>Map of event channels</p>
     * Key is the channel ID e.g. 'knx' for KNX events. Values are {@link EventChannel}
     * that contains the associated {@link InboxComponent} and also creates the corresponding
     * event instances.
     */
    private final Map<String, EventChannel> eventChannelMap = Maps.newHashMap(2);
    /**
     * <p>Map of event keys and its set of {@link InboxComponent}</p>
     */
    private final Map<EventKey, Set<InboxComponent>> inboxMap = Maps.newHashMap(100);

    /**
     * Internal Router Implementation
     */
    private final RouterInternal routerInternal = new RouterInternal();

    private Router() {
        register(new VariableEventChannel());
    }

    /**
     * Creates a new instance of Router with default event channels
     *
     * @return newly instance of router
     */
    public static Router createDefault() {
        return new Router();
    }

    /**
     * Registers the {@link EventChannel} for routing capabilities.
     * <p>
     * <u>Examples:</u>
     * <ul>
     *     <li>{@link VariableEventChannel} for variable events</li>
     *     <li>{@link KnxEventChannel} for KNX events</li>
     * </ul>
     *
     * @param eventChannel the {@link EventChannel} to be registered
     */
    public void register(final EventChannel eventChannel) {
        eventChannelMap.put(eventChannel.getChannel(), eventChannel);
        log.debug("Event channel registered for '{}': {}", eventChannel.getChannel(), eventChannel);
    }

    /**
     * Sends the {@link Event} to inbound channel which activates the routing.
     *
     * @param event event to be considered for routing
     * @return a Future representing pending completion of the task and returns immutable list of all workflow
     */
    public CompletableFuture<List<Workflow>> inbound(final Event event) {
        final var workflowFuture = routerInternal.submit(
                new RouterContext(this, getInboxComponents(event), event)
        );

        // asynchronously updating the workflow in the data source
        workflowFuture.thenAcceptAsync(
                workflows -> {
                    Sleeper.seconds(20);
                    log.debug("TODO: INBOUND record workflow: {}", workflows);
                },
                executor
        );

        return workflowFuture;
    }

    /**
     * Sends the {@link Event} to outbound channel
     *
     * @param event event to be submitted to outbound channel
     */
    public void outbound(final Event event) {
        final var eventChannel = getEventChannel(event);
        CompletableFuture
                // asynchronously sending the event to outbound event channel
                .runAsync(() -> eventChannel.outbound(event), executor)
                // asynchronously updating the workflow in the data source
                .thenRunAsync(() -> {
                    Sleeper.seconds(20);
                    log.debug("TODO: OUTBOUND add to database: {}", event);
                });
    }

    /**
     * Creates a link between the source {@link Pin} and
     * the target {@link Pin}
     *
     * @param source the source where value should come from (e.g. output pin, ...)
     * @param target the target that should receive the value (e.g. input pin, ...)
     */
    public void link(final Pin source, final Pin target) {
        routerInternal.link(source, target);
    }

    /**
     * Removes the link between the source {@link Pin} and
     * the target {@link Pin}. If the link doesn't exists
     * or has been removed previously, then it has no effect.
     *
     * @param source the source where value should come from (e.g. output pin, ...)
     * @param target the target that should receive the value (e.g. input pin, ...)
     */
    public void unlink(final Pin source, final Pin target) {
        routerInternal.unlink(source, target);
    }

    /**
     * Removes all links that is linked to the {@link Pin}.
     *
     * @param pin the pin that should be unlinked
     */
    public void unlink(final Pin pin) {
        routerInternal.unlink(pin);
    }

    /**
     * Registers the component for routing purposes
     *
     * @param component component to be registered
     */
    public void register(final Component component) {
        // assign inbox components to the appropriate event channels
        if (component instanceof InboxComponent) {
            registerInboxComponent((InboxComponent) component);
        }
        routerInternal.register(component);
    }

    /**
     * De-registers the component so that it won't be considered
     * for routing anymore.
     *
     * @param component component to be de-registered
     */
    public void deregister(final Component component) {
        // de-register inbox components
        if (component instanceof InboxComponent) {
            deregisterInboxComponent((InboxComponent) component);
        }
        routerInternal.deregister(component);
    }

    /**
     * Returns a list of {@link Pin} that are linked with given {@link Pin}
     *
     * @param pin the given pin to gather linked pins
     * @return an immutable list of linked pins
     */
    public List<Pin> getLinkedPins(final Pin pin) {
        return routerInternal.getLinkedPins(pin);
    }

    /**
     * Returns the suitable {@link EventChannel} for {@link Event}
     *
     * @param event the event to be used for look up of {@link EventChannel}
     * @return the event channel
     */
    private EventChannel getEventChannel(final Event event) {
        final var eventChannel = event.getKey().getChannel();
        return Preconditions.checkNonNull(eventChannelMap.get(eventChannel),
                "No suitable event channel found for: {}", eventChannel);
    }

    /**
     * Returns a set of suitable {@link InboxComponent} for given {@link Event}
     *
     * @param event event containing channel and key to find suitable {@link InboxComponent}
     * @return a set of inbox components, may be {@code null} if no inbox component was registered for key
     */
    private Set<InboxComponent> getInboxComponents(final Event event) {
        final var inboxComponents = inboxMap.get(event.getKey());
        if (inboxComponents != null) {
            return inboxComponents;
        }
        return Set.of();
    }

    /**
     * Registers the {@link InboxComponent} to inbox component map.
     *
     * @param inboxComponent inbox component to be registered
     */
    private void registerInboxComponent(final InboxComponent inboxComponent) {
        inboxMap.computeIfAbsent(inboxComponent.getEventKey(), k -> new HashSet<>())
                .add(inboxComponent);
        log.debug("Inbox Component registered for '{}' channel: {}", inboxComponent.getEventKey(), inboxComponent);
    }

    /**
     * De-registers the {@link InboxComponent} form inbox component map.
     *
     * @param inboxComponent inbox component to be de-registered
     */
    private void deregisterInboxComponent(final InboxComponent inboxComponent) {
        final var inboxComponents = inboxMap.get(inboxComponent.getEventKey());
        if (inboxComponents != null) {
            inboxComponents.remove(inboxComponent);
        }
        log.debug("Inbox Component de-registered for '{}' channel: {}", inboxComponent.getEventKey(), inboxComponent);
    }
}
