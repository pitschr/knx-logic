package li.pitschmann.knx.api.v1.controllers;

import io.javalin.http.Context;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.json.DiagramRequest;
import li.pitschmann.knx.api.v1.json.DiagramResponse;
import li.pitschmann.knx.api.v1.services.DiagramService;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.diagram.DiagramImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller about the diagram containing components
 *
 * @author PITSCHR
 */
public final class DiagramController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(DiagramController.class);
    private final DiagramService diagramService;
    private final UIDRegistry uidRegistry;

    public DiagramController(final DiagramService diagramService,
                             final UIDRegistry uidRegistry) {
        this.diagramService = Objects.requireNonNull(diagramService);
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
    }


    /**
     * Returns all diagram
     *
     * @param ctx context
     */
    public void getAll(final Context ctx) {
        LOG.trace("Get all diagrams");

        // find all diagrams
        // if specified, start / limit parameters are used to slice the returned list
        final var diagrams = limitAndGetAsList(ctx, uidRegistry.getDiagrams());

        final var responses = diagrams.stream()
                .map(DiagramResponse::from)
                .collect(Collectors.toUnmodifiableList());
        LOG.debug("Diagrams: {}", responses);

        // returns json array of all diagram responses
        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(responses);
    }

    /**
     * Returns response about a specific diagram by its {@code uid}
     *
     * @param ctx context
     * @param uid the diagram uid
     */
    public void getOne(final Context ctx, final String uid) {
        LOG.trace("Get diagram by UID: {}", uid);

        final var diagram = findDiagramByUID(ctx, uid);
        if (diagram == null) {
            return;
        }
        // TODO: Check if diagram exists
        // TODO: get individual diagram (note, component ids)

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(DiagramResponse.from(diagram));
    }

    /**
     * Creates a new diagram based on {@link DiagramRequest}
     *
     * @param ctx     context
     * @param request the request object containing all information to create a new diagram
     */
    public void create(final Context ctx, final DiagramRequest request) {
        LOG.trace("Create new diagram: {}", request);

        // creates a new logic diagram
        final var diagram = new DiagramImpl();
        diagram.setName(request.getName());
        diagram.setDescription(request.getDescription());

        diagramService.insertDiagram(diagram);
        uidRegistry.register(diagram);

        ctx.status(HttpServletResponse.SC_CREATED);
        ctx.json(DiagramResponse.from(diagram));
    }

    /**
     * Updates an existing diagram based on {@link DiagramRequest}
     *
     * @param uid     the UID of diagram to be updated; may not be null
     * @param request the request object containing information to update the existing diagram
     */
    public void update(final Context ctx, final String uid, final DiagramRequest request) {
        LOG.trace("Updates an existing diagram '{}': {}", uid, request);

        final var diagram = (DiagramImpl) findDiagramByUID(ctx, uid);
        if (diagram == null) {
            return;
        }

        if (request.getName() != null) {
            diagram.setName(request.getName());
        }
        if (request.getDescription() != null) {
            diagram.setDescription(request.getDescription());
        }

        diagramService.updateDiagram(diagram);

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(DiagramResponse.from(diagram));
    }

    /**
     * Deletes a specific diagram. This method is idempotent, if the plan
     * doesn't exist, no effect.
     *
     * @param ctx context
     * @param uid the uid of diagram to be deleted
     */
    public void delete(final Context ctx, final String uid) {
        LOG.trace("Delete Diagram by UID: {}", uid);
        Preconditions.checkNonNull(uid, "UID for diagram delete not provided.");

        final var diagram = findDiagramByUID(ctx, uid);
        if (diagram == null) {
            return;
        }

        // delete components (and corresponding connectors, pins, ...) first
        diagramService.getDiagramComponents(diagram, u -> uidRegistry.getComponent(u.toString()))
                .forEach(component -> {
                    diagramService.deleteDiagramComponent(component);
                    uidRegistry.deregister(component);
                });

        // then delete diagram
        diagramService.deleteDiagram(diagram);
        uidRegistry.deregister(diagram);

        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Returns a {@link Diagram} if found, otherwise {@code null}
     * and error message in {@link Context}
     *
     * @param ctx context
     * @param uid UID of diagram for look up
     * @return Diagram if found, otherwise {@code null}
     */
    private Diagram findDiagramByUID(final Context ctx, final String uid) {
        Diagram diagram = null;
        if (uid == null || uid.isBlank()) {
            setBadRequest(ctx, "No diagram UID provided");
        } else {
            diagram = uidRegistry.getDiagram(uid);
            if (diagram == null) {
                setNotFound(ctx, "No diagram found with UID: %s", uid);
            }
        }
        return diagram;
    }
}
