package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.services.LinkService;
import io.javalin.http.Context;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Controller for linking between pins / connectors.
 *
 * @author PITSCHR
 */
public class LinkController {
    private static final Logger LOG = LoggerFactory.getLogger(LinkController.class);
    private final UidRegistry uidRegistry;
    private final LinkService linkService;

    public LinkController(final UidRegistry uidRegistry, final LinkService linkService) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.linkService = Objects.requireNonNull(linkService);
    }

    /**
     * Creates a new link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link already exists, no effect.
     *
     * @param ctx          context
     * @param sourcePinUid source pin UID; may not be null
     * @param targetPinUid target pin UID; may not be null
     */
    public void addLink(final Context ctx, final String sourcePinUid, final String targetPinUid) {
        LOG.debug("Add Link: {}", ctx);

        final var sourcePin = Preconditions.checkNonNull(uidRegistry.findPinByUID(sourcePinUid),
                "Source Pin UID not found: {}", sourcePinUid);

        final var targetPin = Preconditions.checkNonNull(uidRegistry.findPinByUID(targetPinUid),
                "Target Pin UID not found: {}", targetPinUid);

        try {
            linkService.addLink(sourcePin, targetPin);
            ctx.status(HttpServletResponse.SC_NO_CONTENT);
        } catch (final Exception e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Removes an existing link between source {@link Pin} and target {@link Pin}.
     * This method is idempotent, if the link doesn't exists (anymore), no effect.
     *
     * @param ctx          context
     * @param sourcePinUid source pin UID; may not be null
     * @param targetPinUid target pin UID; may not be null
     */
    public void deleteLink(final Context ctx, final String sourcePinUid, final String targetPinUid) {
        LOG.debug("Delete Link: {}", ctx);

        final var sourcePin = Preconditions.checkNonNull(uidRegistry.findPinByUID(sourcePinUid),
                "Source Pin UID not found: {}", sourcePinUid);

        final var targetPin = Preconditions.checkNonNull(uidRegistry.findPinByUID(targetPinUid),
                "Target Pin UID not found: {}", targetPinUid);

        try {
            linkService.deleteLink(sourcePin, targetPin);
            ctx.status(HttpServletResponse.SC_NO_CONTENT);
        } catch (final Exception e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Deletes all links on the given {@link Pin}
     *
     * @param ctx    context
     * @param pinUid the pin UID; may not be null
     */
    public void deleteLinks(final Context ctx, final String pinUid) {
        LOG.debug("Delete Links: {}", ctx);

        final var pin = Preconditions.checkNonNull(uidRegistry.findPinByUID(pinUid),
                "Pin UID not found: {}", pinUid);

        try {
            linkService.deleteLinks(pin);
            ctx.status(HttpServletResponse.SC_NO_CONTENT);
        } catch (final Exception e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * Returns all pins that is linked to the {@link Pin}
     *
     * @param ctx    context
     * @param pinUid the pin UID; may not be null
     */
    public void getLinks(final Context ctx, final String pinUid) {
        LOG.debug("Get Links: {}", ctx);

        final var pin = Preconditions.checkNonNull(uidRegistry.findPinByUID(pinUid),
                "Pin UID not found: {}", pinUid);

        try {
            final var pinUids = linkService.getLinkedUIDs(pin);
            ctx.json(pinUids);
            ctx.status(HttpServletResponse.SC_OK);
        } catch (final Exception e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
