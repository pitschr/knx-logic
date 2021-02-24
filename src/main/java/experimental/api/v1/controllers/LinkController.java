package experimental.api.v1.controllers;

import experimental.UidRegistry;
import io.javalin.http.Context;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for linking between pins / connectors.
 *
 * @author PITSCHR
 */
public class LinkController {
    private static final Logger LOG = LoggerFactory.getLogger(LinkController.class);
    private final UidRegistry uidRegistry;
    private final Router router;

    public LinkController(final UidRegistry uidRegistry, final Router router) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.router = Objects.requireNonNull(router);
    }

    /**
     * Creates a new link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link already exists, no effect.
     *
     * @param ctx       context
     * @param sourcePinUid source pin UID; may not be null
     * @param targetPinUid target pin UID; may not be null
     */
    public void addLink(final Context ctx, final String sourcePinUid, final String targetPinUid) {
        LOG.trace("Add Link: {}", ctx);

        final var sourcePin = uidRegistry.findPinByUID(sourcePinUid);
        final var targetPin = uidRegistry.findPinByUID(targetPinUid);
        // Source and target pins must be provided
        if (sourcePin == null || targetPin == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        router.link(sourcePin, targetPin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Removes an existing link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link doesn't exists (anymore), no effect.
     *
     * @param ctx       context
     * @param sourcePinUid source pin UID; may not be null
     * @param targetPinUid target pin UID; may not be null
     */
    public void deleteLink(final Context ctx, final String sourcePinUid, final String targetPinUid) {
        LOG.trace("Delete Link: {}", ctx);

        final var sourcePin = uidRegistry.findPinByUID(sourcePinUid);
        final var targetPin = uidRegistry.findPinByUID(targetPinUid);
        // Source and target pins must be provided
        if (sourcePin == null || targetPin == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        router.unlink(sourcePin, targetPin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Returns all pins that listens on the source {@link Pin}
     *
     * @param ctx       context
     * @param pinUid the pin UID; may not be null
     */
    public void getLinks(final Context ctx, final String pinUid) {
        final var pin = uidRegistry.findPinByUID(pinUid);

        // Pin must be provided
        if (pin == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final var pinUids = router.getSubscribers(pin).stream().map(Pin::getUid).collect(Collectors.toList());
        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(pinUids);
    }

    /**
     * Deletes all links on the given {@link Pin}
     *
     * @param ctx       context
     * @param pinUid the pin UID; may not be null
     */
    public void deleteLinks(final Context ctx, final String pinUid) {
        final var pin = uidRegistry.findPinByUID(pinUid);

        // Pin must be provided
        if (pin == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        router.unlink(pin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }
}
