package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.CreateDiagramRequest;
import experimental.api.v1.json.DiagramResponse;
import experimental.api.v1.services.DiagramService;
import io.javalin.http.Context;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.diagram.DiagramImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller about the diagram containing components
 *
 * @author PITSCHR
 */
public final class DiagramController {
    private static final Logger LOG = LoggerFactory.getLogger(DiagramController.class);
    private final DiagramService diagramService;

    public DiagramController(final DiagramService diagramService) {
        this.diagramService = Objects.requireNonNull(diagramService);
    }


    /**
     * Returns all diagram
     *
     * @param ctx context
     */
    public void getAll(final Context ctx) {
        LOG.trace("Get all diagrams");

        // returns json array of all diagram responses
        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(UidRegistry.getAllDiagrams().stream().map(DiagramResponse::from).collect(Collectors.toList()));
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
     * Creates a new diagram based on {@link CreateDiagramRequest}
     *
     * @param ctx     context
     * @param request the request object containing all information to create a new diagram
     */
    public void create(final Context ctx, final CreateDiagramRequest request) {
        LOG.trace("Create new diagram: {}", request);

        // creates a new logic diagram
        final var diagram = new DiagramImpl();
        diagram.setName(request.getName());
        diagram.setDescription(request.getDescription());
        UidRegistry.register(diagram);

        ctx.status(HttpServletResponse.SC_CREATED);
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

        final var diagram = UidRegistry.findDiagramByUID(uid);
        if (diagram == null) {
            ctx.status(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        UidRegistry.deregister(diagram);
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
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of("message", "No diagram UID provided."));
        } else {
            diagram = UidRegistry.findDiagramByUID(uid);
            if (diagram == null) {
                ctx.status(HttpServletResponse.SC_NOT_FOUND);
                ctx.json(Map.of(
                        "message",
                        String.format("No diagram found with UID: %s", uid))
                );
            }
        }
        return diagram;
    }
}