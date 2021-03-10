package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.ComponentFactory;
import experimental.api.v1.json.ComponentResponse;
import experimental.api.v1.json.ConnectorResponse;
import experimental.api.v1.json.CreateComponentRequest;
import experimental.api.v1.json.PinResponse;
import experimental.api.v1.services.ComponentService;
import io.javalin.http.Context;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorAware;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for {@link Component} (Logic, Inbox, Outbox)
 *
 * @author PITSCHR
 */
public class ComponentController {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentController.class);

    private final UidRegistry uidRegistry;
    private final ComponentFactory componentFactory;
    private final ComponentService componentService;

    public ComponentController(final UidRegistry uidRegistry,
                               final ComponentFactory componentFactory,
                               final ComponentService componentService) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.componentFactory = Objects.requireNonNull(componentFactory);
        this.componentService = Objects.requireNonNull(componentService);
    }

    /**
     * Returns all {@link Component}
     *
     * @param ctx context
     */
    public void getComponents(final Context ctx) {
        LOG.trace("Return all components");

        // return and find all components
        final var components = uidRegistry.getAllComponents();

        final var responses = new ArrayList<>(components.size());
        for (final var component : components) {
            responses.add(toComponentResponse(component));
        }

        // TODO: filter? limit?

        ctx.status(200);
        ctx.json(responses);
    }

    /**
     * Return individual {@link Component} by UID
     *
     * @param ctx          context
     * @param componentUid component uid to be returned as JSON representation
     */
    public void getComponent(final Context ctx, final String componentUid) {
        LOG.trace("Get Info for Component UID: {}", componentUid);
        Preconditions.checkNonNull(componentUid, "UID for component not provided.");

        // find the component by uid
        final var component = uidRegistry.findComponentByUID(componentUid);
        if (component == null) {
            ctx.status(404);
            return;
        }

        // return component
        ctx.status(200);
        ctx.json(toComponentResponse(component));
    }

    /**
     * Creates a new {@link Component}
     *
     * @param ctx     context
     * @param request request containing data to create a new component
     */
    public void createComponent(final Context ctx, final CreateComponentRequest request) {
        LOG.trace("Creates new component for: {}", request);

        // create component
        final Component component;
        if ("logic".equalsIgnoreCase(request.getType())) {
            // create logic
            component = componentFactory.createLogic(request.getData());
            LOG.debug("Logic Component added: {}", component);
        } else if ("inbox".equalsIgnoreCase(request.getType())) {
            // create inbox
            component = componentFactory.createInbox(request.getEvent(), request.getData());
            LOG.debug("Inbox Component added: {}", component);
        } else if ("outbox".equalsIgnoreCase(request.getType())) {
            // create outbox
            component = componentFactory.createOutbox(request.getEvent(), request.getData());
            LOG.debug("Outbox Component added: {}", component);
        } else {
            ctx.status(400);
            throw new AssertionError(
                    "Unsupported type: " + request.getType() + ". Supported are: logic, inbox and outbox."
            );
        }

        componentService.addComponent(component);

        // register the component
        uidRegistry.registerComponent(component);

        LOG.debug("Component registered: {}", component);

        ctx.status(201);
        ctx.json(toComponentResponse(component));
    }


    /**
     * Deletes the individual {@link Component} by its UID.
     * This method is idempotent. If the component doesn't exists, no effect.
     *
     * @param ctx          context
     * @param componentUid component uid to be deleted
     */
    public void deleteComponent(final Context ctx, final String componentUid) {
        LOG.trace("Delete for Component UID: {}", componentUid);
        Preconditions.checkNonNull(componentUid, "UID for component delete not provided.");

        final var component = uidRegistry.findComponentByUID(componentUid);
        if (component != null) {
            // TODO: check if component has at least one link -> protected?
            uidRegistry.deregisterComponent(component);
            componentService.removeComponent(component);
        }

        ctx.status(204);
    }

    private ComponentResponse toComponentResponse(final Component component) {
        final var response = new ComponentResponse();
        response.setUid(component.getUid().toString());
        response.setClassName(component.getWrappedObject().getClass().getName());
        response.setInputs(getInputConnectors(component).stream().map(this::toConnectorResponse).collect(Collectors.toList()));
        response.setOutputs(getOutputConnectors(component).stream().map(this::toConnectorResponse).collect(Collectors.toList()));
        return response;
    }

    private List<Connector> getInputConnectors(final Component component) {
        return component instanceof InputConnectorAware ? ((InputConnectorAware) component).getInputConnectors() : List.of();
    }

    private List<Connector> getOutputConnectors(final Component component) {
        return component instanceof OutputConnectorAware ? ((OutputConnectorAware) component).getOutputConnectors() : List.of();
    }

    private ConnectorResponse toConnectorResponse(final Connector connector) {
        final var connectorResponse = new ConnectorResponse();
        connectorResponse.setName(connector.getDescriptor().getName());
        connectorResponse.setPinType(connector.getDescriptor().getFieldType().getSimpleName().toLowerCase());
        connectorResponse.setPins(toPinResponses(connector));
        connectorResponse.setDynamic(connector instanceof DynamicConnector);
        return connectorResponse;
    }

    private List<PinResponse> toPinResponses(final Connector connector) {
        final var pins = connector.getPinStream().collect(Collectors.toList());
        final var pinResponses = new ArrayList<PinResponse>(pins.size());
        for (final var pin : pins) {
            final var pinResponse = new PinResponse();
            pinResponse.setUid(pin.getUid().toString());
            pinResponse.setValue(String.valueOf(pin.getValue()));
            pinResponses.add(pinResponse);
        }
        return pinResponses;
    }
}
