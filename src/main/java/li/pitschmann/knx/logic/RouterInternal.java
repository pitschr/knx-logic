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
     * Defines the linking between one {@link Pin} and other {@link Pin}s.
     * </p>
     * <p>
     * The map is bi-directional. By giving {@link Pin} as key we know to which other
     * {@link Pin} it is connected.
     * </p>
     */
    private final Map<Pin, Set<Pin>> linkMap = Maps.newHashMap(1000);

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
        linkMap.computeIfAbsent(source, key -> new LinkedHashSet<>()) // linked to be used because of ordering guarantee
                .add(target);

        linkMap.computeIfAbsent(target, key -> new LinkedHashSet<>())
                .add(source);

        // for detailed logging purposes only to see the mapping of source/target pins
        if (log.isTraceEnabled()) {
            log.trace(
                    "LINK:\n{}",
                    linkMap.entrySet()
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
        final var otherPins = linkMap.get(pin);
        if (otherPins != null) {
            // copy of list because we change the routingMap in method 'unlink' (avoid ConcurrentModificationException)
            List.copyOf(otherPins).forEach(sourcePin -> unlink(sourcePin, pin));
        }

        // for detailed logging purposes only to see the mapping of source/target pins
        if (log.isTraceEnabled()) {
            log.trace(
                    "UNLINK:\n{}",
                    linkMap.entrySet()
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
        final var targets = linkMap.get(source);
        if (targets != null) {
            targets.remove(target);
            if (targets.isEmpty()) {
                linkMap.remove(source); // remove the key when there are no targets available anymore
            }
        }

        final var sources = linkMap.get(target);
        if (sources != null) {
            sources.remove(source);
            if (sources.isEmpty()) {
                linkMap.remove(target); // remove the key when there are no targets available anymore
            }
        }

        log.debug("Remove Link: {} <=> {}", source.getUid(), target.getUid());
    }

    /**
     * Returns a list of {@link Pin} that are linked with given {@link Pin}
     *
     * @param pin the given pin to gather linked pins
     * @return an immutable list of linked pins, or empty list if no links found
     */
    public List<Pin> getLinkedPins(final Pin pin) {
        final var linkedPins = linkMap.get(pin);
        if (linkedPins != null && !linkedPins.isEmpty()) {
            return List.copyOf(linkedPins);
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
     * @param source       the current {@link UIDAware} (e.g. Pin) where the value comes from
     * @param value        the value to be processed; may be null
     * @param workflow     the workflow for pass-through
     * @param workflowList list of workflow to be collected and returned
     */
    private void forwardInternal(final RouterContext context,
                                 final Pin source,
                                 final @Nullable Object value,
                                 final Workflow workflow,
                                 final List<Workflow> workflowList) {
        final var targets = linkMap.get(source);
        if (targets == null || targets.isEmpty()) {
            log.debug("Dead End for '{}': {}", source, workflow);
            workflowList.add(workflow);
        } else {
            for (final var target : targets) {
                if (log.isDebugEnabled()) {
                    log.debug("{} ==> {}", source.getUid(), target.getUid());
                }

                final var newWorkflow = workflow.add(target, value);

                // convert the value if necessary
                final var newValue = convertValue(source, target, value);
                forwardPin(context, target, newValue, newWorkflow, workflowList);
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
     * @param source the pin where the value comes from
     * @param target   the pin that should set with the value
     * @param value     the given value; may be null
     * @return the value; may not be null
     * @throws RouterException in case the conversion was not possible
     */
    private Object convertValue(final Pin source,
                                final Pin target,
                                final @Nullable Object value) {
        final var targetFieldType = target.getDescriptor().getFieldType();
        if (value == null) {
            final var newValue = ValueHelper.getDefaultValueFor(targetFieldType);
            log.debug("Converted NULL value to '{}' (type: {}): source={}, target={}",
                    newValue, targetFieldType, source.getUid(), target.getUid());
            return newValue;
        }

        final var valueType = value.getClass();

        // OK, class is assignable - no conversion required
        if (targetFieldType.isAssignableFrom(valueType)) {
            log.debug("No conversion required for value '{}' (type: {}=>{}): source={}, target={}",
                    value, valueType, targetFieldType, source.getUid(), target.getUid());
            return value;
        }
        // Conversion required, will try with conversion
        else {
            Object convertedValue = null;
            // Any object -> String
            if (targetFieldType == String.class) {
                convertedValue = String.valueOf(value);
            }
            // Boolean -> Number (1, 1.0)
            else if (valueType == Boolean.class
                    && Number.class.isAssignableFrom(targetFieldType)) {
                convertedValue = Boolean.TRUE.equals(value) ? 1 : 0;
            }
            // Number (1, 1.0) -> Boolean
            else if (Number.class.isAssignableFrom(valueType) && targetFieldType == Boolean.class) {
                final var iValue = (int)value;
                if (iValue == 0) {
                    convertedValue = Boolean.FALSE;
                } else if (iValue == 1) {
                    convertedValue = Boolean.TRUE;
                }
            }

            // Conversion was successful?
            if (convertedValue != null) {
                log.debug("Converted from '{}' to '{}' (type: {}=>{}): source={}, target={}",
                        value, convertedValue, valueType, targetFieldType, source.getUid(), target.getUid());
                return convertedValue;
            }
        }

        // not compatible
        throw new RouterException(
                String.format(
                        "The type of value '%s' (value: %s) is not compatible with '%s': source=%s => target=%s",
                        valueType, value, targetFieldType, source.getUid(), target.getUid()
                )
        );
    }
}
