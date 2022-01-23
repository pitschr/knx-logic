package li.pitschmann.knx.api.v1.controllers;

import io.javalin.http.Context;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.ComponentFactory;
import li.pitschmann.knx.api.v1.json.ComponentRequest;
import li.pitschmann.knx.api.v1.json.ComponentResponse;
import li.pitschmann.knx.api.v1.services.ComponentService;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for {@link Component} (Logic, Inbox, Outbox)
 *
 * @author PITSCHR
 */
public final class ComponentController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentController.class);

    private final ComponentFactory componentFactory;
    private final ComponentService componentService;
    private final UIDRegistry uidRegistry;

    public ComponentController(final ComponentService componentService,
                               final ComponentFactory componentFactory,
                               final UIDRegistry uidRegistry) {
        this.componentService = Objects.requireNonNull(componentService);
        this.componentFactory = Objects.requireNonNull(componentFactory);
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
    }

    /**
     * Returns all {@link Component}
     *
     * @param ctx context
     */
    public void getAll(final Context ctx) {
        LOG.trace("Return all components");

        // return and find all components
        final var components = uidRegistry.getComponents();

        final var responses = components.stream()
                .map(ComponentResponse::from)
                .collect(Collectors.toUnmodifiableList());

        // TODO: filter? limit?

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(responses);
    }

    /**
     * Return individual {@link Component} by UID
     *
     * @param ctx context
     * @param uid component uid to be returned as JSON representation
     */
    public void getOne(final Context ctx, final String uid) {
        LOG.trace("Get Info for Component UID: {}", uid);

        final var component = findComponentByUID(ctx, uid);
        if (component == null) {
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(ComponentResponse.from(component));
    }

    /**
     * Creates a new {@link Component}
     *
     * @param ctx     context
     * @param request request containing data to create a new component
     */
    public void create(final Context ctx, final ComponentRequest request) {
        LOG.trace("Creates new component for: {}", request);

        // create component
        final Component component;
        if ("logic".equalsIgnoreCase(request.getType())) {
            // create logic
            try {
                component = componentFactory.createLogic(request.getData());
                LOG.debug("Logic Component added: {}", component);
            } catch (final NoLogicClassFound ex) {
                setBadRequest(ctx, ex.getMessage());
                return;
            }
        } else if ("inbox".equalsIgnoreCase(request.getType())) {
            // create inbox
            component = componentFactory.createInbox(request.getEvent(), request.getData());
            LOG.debug("Inbox Component added: {}", component);
        } else if ("outbox".equalsIgnoreCase(request.getType())) {
            // create outbox
            component = componentFactory.createOutbox(request.getEvent(), request.getData());
            LOG.debug("Outbox Component added: {}", component);
        } else {
            LOG.error("Unsupported Component Type: {}", request.getType());
            setBadRequest(
                    ctx,
                    "Unsupported Component Type: %s. Supported are: logic, inbox and outbox.",
                    request.getType()
            );
            return;
        }

        componentService.addComponent(component);

        // register the component
        uidRegistry.register(component);

        LOG.debug("Component registered: {}", component);

        ctx.status(HttpServletResponse.SC_CREATED);
        ctx.json(ComponentResponse.from(component));
    }


    /**
     * Deletes the individual {@link Component} by its UID.
     * This method is idempotent. If the component doesn't exists, no effect.
     *
     * @param ctx context
     * @param uid component uid to be deleted
     */
    public void delete(final Context ctx, final String uid) {
        LOG.trace("Delete for Component UID: {}", uid);
        Preconditions.checkNonNull(uid, "UID for component delete not provided.");

        final var component = findComponentByUID(ctx, uid);
        if (component == null) {
            return;
        }

        // TODO: check if component has at least one link -> protected?
        uidRegistry.deregister(component);
        componentService.removeComponent(component);

        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Returns a {@link Component} if found, otherwise {@code null}
     * and error message in {@link Context}
     *
     * @param ctx context
     * @param uid UID of component for look up
     * @return Component if found, otherwise {@code null}
     */
    private Component findComponentByUID(final Context ctx, final String uid) {
        Component component = null;
        if (uid == null || uid.isBlank()) {
            setBadRequest(ctx, "No component UID provided");
        } else {
            component = uidRegistry.getComponent(uid);
            if (component == null) {
                setNotFound(ctx, "No component found with UID: %s", uid);
            }
        }
        return component;
    }
}
