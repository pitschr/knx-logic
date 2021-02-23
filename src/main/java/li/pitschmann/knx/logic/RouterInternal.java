package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.components.ExecutableComponent;
import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.OutboxComponent;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorAware;
import li.pitschmann.knx.logic.event.Event;
import li.pitschmann.knx.logic.exceptions.RouterException;
import li.pitschmann.knx.logic.helpers.ValueHelper;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.PinAware;
import li.pitschmann.knx.logic.uid.UIDAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * The internal router implementation that takes care
 * about the single workflow and linked {@link Component}s
 * and {@link Pin}s
 * <p>
 * This class is called by {@link Router} instance and
 * should not be accessible from outside.
 *
 * @author PITSCHR
 */
final class RouterInternal {
    private static final Logger log = LoggerFactory.getLogger(RouterInternal.class);

    /**
     * The thread executors for parallel routing
     */
    private final ExecutorService executor = Executors.newFixedThreadPool(8);

    /**
     * <p>
     * Defines the linking between the source {@link Pin} and the subscriber
     * list of target {@link Pin}s. Source may be e.g. pin of {@link InboxComponent}
     * or an <i>output</i> {@link Pin}
     * </p>
     * <p>
     * This is the map to be used for usual routing to identify what are
     * subsequent {@link Pin}s and call them (by setting value etc.).
     * </p>
     * <p>
     * Key is an <strong>output</strong> {@link Pin}, Values is a set of
     * <strong>input</strong> {@link Pin}s
     * </p>
     */
    private final Map<Pin, Set<Pin>> routingMap = Maps.newHashMap(1000);

    /**
     * <p>
     * Mapping between {@link Connector}s (input and output) and the
     * {@link LogicComponent} as a wrapper of a concrete logic.
     * This will be used to invoke the {@link LogicComponent}
     * which holds references of {@link Connector}s to be invoked.
     * </p>
     */
    private final Map<Connector, Component> connectorComponentMap = Maps.newHashMap(1000);

    /**
     * Package-protected constructor
     */
    RouterInternal() {
    }

    /**
     * Registers the component which should be considered for
     * the routing.
     *
     * @param component component to be registered
     */
    public void register(final Component component) {
        // register all input connectors
        if (component instanceof ConnectorAware) {
            final var connectorAware = (ConnectorAware) component;
            connectorAware.getConnectors().forEach(connector -> connectorComponentMap.put(connector, component));
            log.debug("Component registered: {}", component);
        }
    }

    /**
     * De-registers the component as they are not relevant for
     * routing anymore (e.g. when deleted)
     *
     * @param component component to be de-registered
     */
    public void deregister(final Component component) {
        // de-registers mapping of source->target and source<-target
        if (component instanceof PinAware) {
            final var pinAware = (PinAware) component;
            pinAware.getPins().forEach(this::unlink);
        }
        // de-register all input connectors
        if (component instanceof ConnectorAware) {
            final var connectorAware = (ConnectorAware) component;
            connectorAware.getConnectors().forEach(connectorComponentMap::remove);
            log.debug("Component de-registered: {}", component);
        }
    }

    /**
     * Creates a link between the source {@link Pin} and
     * the target {@link Pin}. If the link doesn't exists
     * or has been removed previously, then it has no effect.
     *
     * @param source the source where value should come from (e.g. output pin, ...)
     * @param target the target that should receive the value (e.g. input pin, ...)
     */
    public void link(final Pin source, final Pin target) {
        routingMap
                .computeIfAbsent(source, key -> new LinkedHashSet<>()) // linked to be used because of ordering guarantee
                .add(target);

        routingMap
                .computeIfAbsent(target, key -> new HashSet<>())       // for reverse the ordering is not important
                .add(source);

        // for detailed logging purposes only to see the mapping of source/target pins
        if (log.isTraceEnabled()) {
            log.trace(
                    "LINK:\n{}",
                    routingMap.entrySet()
                            .stream()
                            .map(e ->
                                    "Source: " + e.getKey().getUid() + ", Targets: " + e.getValue().stream().map(Pin::getUid).collect(Collectors.toList()))
                            .collect(Collectors.joining(System.lineSeparator()))
            );
        }

        log.debug("Add Link: {} <=> {}", source.getUid(), target.getUid());
    }

    /**
     * Removes all links for given {@link Pin}.
     * If there are no links to the {@link Pin}, then it has no effect.
     *
     * @param pin the pin whose links should be unlinked
     */
    public void unlink(final Pin pin) {
        final var otherPins = routingMap.get(pin);
        if (otherPins != null) {
            // copy of list because we change the routingMap in method 'unlink' (avoid ConcurrentModificationException)
            List.copyOf(otherPins).forEach(sourcePin -> unlink(sourcePin, pin));
        }

        // for detailed logging purposes only to see the mapping of source/target pins
        if (log.isTraceEnabled()) {
            log.trace(
                    "UNLINK:\n{}",
                    routingMap.entrySet()
                            .stream()
                            .map(e -> "Source: " + e.getKey().getUid() + ", Targets: " + e.getValue().stream().map(Pin::getUid).collect(Collectors.toList()))
                            .collect(Collectors.joining(System.lineSeparator()))
            );
        }
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
        final var subscribersBySource = routingMap.get(source);
        if (subscribersBySource != null) {
            subscribersBySource.remove(target);
            if (subscribersBySource.isEmpty()) {
                routingMap.remove(source); // remove the key when there are no targets available anymore
            }
        }

        final var subscribersByTarget = routingMap.get(target);
        if (subscribersByTarget != null) {
            subscribersByTarget.remove(source);
            if (subscribersByTarget.isEmpty()) {
                routingMap.remove(target); // remove the key when there are no targets available anymore
            }
        }

        log.debug("Remove Link: {} <=> {}", source.getUid(), target.getUid());
    }

    /**
     * Returns a list of subscribers by given {@link Pin}
     *
     * @param pin the pin as a source to gather connected pins
     * @return an immutable list of connected pins
     */
    public List<Pin> getSubscribers(final Pin pin) {
        final var subscribers = routingMap.get(pin);
        if (subscribers != null && !subscribers.isEmpty()) {
            return List.copyOf(subscribers);
        }
        return List.of();
    }

    /**
     * Starts the routing of {@link Event} to all suitable {@link InboxComponent}
     * using {@link RouterContext}
     * <p>
     * It will iteratively call all {@link InboxComponent} asynchronously.
     *
     * @param context the context of routing
     * @return a Future representing pending completion of the task and returns immutable list of all workflow
     */
    public CompletableFuture<List<Workflow>> submit(final RouterContext context) {
        final var inboxComponents = context.getInboxComponents();

        if (inboxComponents == null || inboxComponents.isEmpty()) {
            log.debug("No suitable inbox components found for event: {}", context.getEvent());
            return CompletableFuture.completedFuture(List.of());
        } else {
            var workflowFuture = CompletableFuture.<List<Workflow>>supplyAsync(() -> new ArrayList<>(inboxComponents.size()), executor);
            for (final var inboxComponent : inboxComponents) {
                workflowFuture = workflowFuture.thenApplyAsync(workflowList -> {
                    workflowList.addAll(forwardInboxComponent(context, inboxComponent));
                    return workflowList;
                }, executor);
            }
            return workflowFuture;
        }
    }

    /**
     * The loop of forwarding that passed a value from component A to B.
     *
     * @param context      the context of routing
     * @param origin       the current {@link UIDAware} (e.g. Pin) where the value comes from
     * @param value        the value to be processed; may be null
     * @param workflow     the workflow for pass-through
     * @param workflowList list of workflow to be collected and returned
     */
    private void forwardInternal(final RouterContext context,
                                 final Pin origin,
                                 final @Nullable Object value,
                                 final Workflow workflow,
                                 final List<Workflow> workflowList) {
        final var subscribers = routingMap.get(origin);
        if (subscribers == null || subscribers.isEmpty()) {
            log.debug("Dead End for '{}': {}", origin, workflow);
            workflowList.add(workflow);
        } else {
            for (final var subscriber : subscribers) {
                if (log.isDebugEnabled()) {
                    log.debug("{} ==> {}", origin.getUid(), subscriber.getUid());
                }

                final var newWorkflow = workflow.add(subscriber, value);

                // convert the value if necessary
                final var newValue = convertValue(origin, subscriber, value);
                forwardPin(context, subscriber, newValue, newWorkflow, workflowList);
            }
        }
    }

    /**
     * Forwards the value to {@link Pin} of a component and invokes the component.
     * The outputs which are triggered as 'refresh'
     *
     * @param context      the context of routing
     * @param pin          the targeted pin
     * @param value        the actual value to be forwarded
     * @param workflow     the workflow for pass-through
     * @param workflowList list of workflow to be collected and returned
     */
    private void forwardPin(final RouterContext context,
                            final Pin pin,
                            final @Nullable Object value,
                            final Workflow workflow,
                            final List<Workflow> workflowList) {
        // set the value to pin
        pin.setValue(value);

        // find the component that owns the pin/connector for execution
        final var component = connectorComponentMap.get(pin.getConnector());
        if (component instanceof ExecutableComponent) {
            ((ExecutableComponent) component).execute();
        }

        // The workflow ends here when it is an output component
        if (component instanceof OutboxComponent) {
            log.debug("Outbox Component: {}, Workflow: {}", component, workflow);
            forwardOutboxComponent(context, (OutboxComponent) component);
            workflowList.add(workflow);
        }
        // Otherwise forward the values which are marked as refreshed
        else if (component instanceof LogicComponent) {
            for (final var output : ((LogicComponent) component).getOutputPins()) {
                if (output.isRefresh() || output.isAlwaysTrigger()) {
                    final var outputValue = output.getValue();
                    final var newWorkflow = workflow.add(output, outputValue);
                    forwardInternal(context, output, outputValue, newWorkflow, workflowList);
                }
            }
        } else {
            log.warn("Dead End for '{}': {}", pin.getUid(), workflow);
            throw new RouterException("Dead End because of unsupported component: " + component);
        }
    }

    /**
     * Forward value to {@link InboxComponent}
     *
     * @param context        the context of routing
     * @param inboxComponent the targeted input component
     * @return list of workflow during the routing
     */
    private List<Workflow> forwardInboxComponent(final RouterContext context,
                                                 final InboxComponent inboxComponent) {
        final var event = context.getEvent();
        log.debug("Inbox Component: {}, Event: {}", inboxComponent, event);
        inboxComponent.onNext(event.getData());

        final var workflowList = new LinkedList<Workflow>();
        for (final var output : inboxComponent.getOutputPins()) {
            final var outputValue = output.getValue();
            final var newWorkflow = Workflow.create().add(output, outputValue);
            forwardInternal(context, output, outputValue, newWorkflow, workflowList);
        }
        return workflowList;
    }

    /**
     * Forward and finalize the routing to {@link OutboxComponent}. The routing will stop here.
     *
     * @param context         the context of routing
     * @param outboxComponent the last component which is outbox
     */
    private void forwardOutboxComponent(final RouterContext context,
                                        final OutboxComponent outboxComponent) {
        final var data = outboxComponent.execute();
        log.debug("Outbox Component: {}, Data: {}", outboxComponent, data);

        // submits to the outbound
        final var event = new Event(outboxComponent.getEventKey(), data);
        context.getRouter().outbound(event);
    }

    /**
     * If necessary it tries to convert the value taken from {@code originPin} to
     * a type that is supported by {@code nextPin}.
     * <p>
     * This gives a more flexibility to convert e.g. {@link String} values to a {@link Integer}
     * if the string represents digits only.
     * <p>
     * In case the value could not be converted a {@link RouterException} will be thrown.
     *
     * @param originPin the pin where the value comes from
     * @param nextPin   the pin that should set with the value
     * @param value     the given value; may be null
     * @return the value; may not be null
     * @throws RouterException in case the conversion was not possible
     */
    private Object convertValue(final Pin originPin,
                                final Pin nextPin,
                                final @Nullable Object value) {
        final var nextValueClass = nextPin.getDescriptor().getFieldType();
        if (value == null) {
            final var newValue = ValueHelper.getDefaultValueFor(nextValueClass);
            log.debug("Converted NULL value to '{}' for: originPin={}, nextPin={}", newValue, originPin.getUid(), nextPin.getUid());
            return newValue;
        }

        final var valueClass = value.getClass();
        if (nextValueClass.equals(valueClass)) {
            // OK, same class - no conversion required
            return value;
        } else if (nextValueClass.isAssignableFrom(valueClass)) {
            // OK, class is assignable - no conversion required
            log.debug("No conversion required for value '{}' (type: {}=>{}): originPin={}, nextPin={}",
                    value, valueClass, nextValueClass, originPin.getUid(), nextPin.getUid());
            return value;
        } else {
            // Conversion required, will try with conversion
            Object convertedValue = null;
            if (valueClass == Character.class) {
                char castedValue = (char) value;
                if (nextValueClass == String.class) {
                    convertedValue = Character.toString(castedValue);
                }
            }

            // Conversion was successful?
            if (convertedValue != null) {
                log.debug("Converted from '{}' to '{}': {}", valueClass, nextValueClass, convertedValue);
                return convertedValue;
            }
        }

        // not compatible
        throw new RouterException(String.format(
                "The type of value '%s' (value: %s) is not compatible with '%s': " +
                        "originPin=%s => nextPin=%s",
                valueClass, value, nextValueClass,
                originPin.getUid(), nextPin.getUid()
        ));
    }
}
