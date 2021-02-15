package experimental.api.v1.controllers;

import io.javalin.http.Context;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Controller for linking between pins / connectors.
 *
 * @author PITSCHR
 */
public class LinkController {
    private static final Logger LOG = LoggerFactory.getLogger(LinkController.class);
    private final Router router;

    public LinkController(final Router router) {
        this.router = Objects.requireNonNull(router);
    }

    /**
     * Creates a new link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link already exists, no effect.
     *
     * @param ctx       context
     * @param sourcePin source pin
     * @param targetPin target pin
     */
    public void addLink(final Context ctx, final Pin sourcePin, final Pin targetPin) {
        LOG.trace("Add Link: {}", ctx);

        router.link(sourcePin, targetPin);

        // TODO: Return the data of link?

        ctx.status(201);
    }

    /**
     * Removes an existing link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link doesn't exists (anymore), no effect.
     *
     * @param ctx       context
     * @param sourcePin source pin
     * @param targetPin target pin
     */
    public void deleteLink(final Context ctx, final Pin sourcePin, final Pin targetPin) {
        LOG.trace("Delete Link: {}", ctx);

        router.unlink(sourcePin, targetPin);
        ctx.status(204);
    }

}
