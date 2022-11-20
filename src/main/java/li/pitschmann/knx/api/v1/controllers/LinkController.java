package li.pitschmann.knx.api.v1.controllers;

import io.javalin.http.Context;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.services.LinkService;
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
public final class LinkController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(LinkController.class);
    private final LinkService linkService;
    private final UIDRegistry uidRegistry;

    public LinkController(final LinkService linkService,
                          final UIDRegistry uidRegistry) {
        this.linkService = Objects.requireNonNull(linkService);
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
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
        LOG.trace("Add Link: sourceUid={} and targetUid={}", sourcePinUid, targetPinUid);

        final var sourcePin = findPinByUID(ctx, sourcePinUid, PinType.SOURCE);
        if (sourcePin == null) {
            return;
        }

        final var targetPin = findPinByUID(ctx, targetPinUid, PinType.TARGET);
        if (targetPin == null) {
            return;
        }

        linkService.addLink(sourcePin, targetPin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
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
        LOG.trace("Delete Link: sourceUid={} and targetUid={}", sourcePinUid, targetPinUid);

        final var sourcePin = findPinByUID(ctx, sourcePinUid, PinType.SOURCE);
        if (sourcePin == null) {
            return;
        }

        final var targetPin = findPinByUID(ctx, targetPinUid, PinType.TARGET);
        if (targetPin == null) {
            return;
        }

        linkService.deleteLink(sourcePin, targetPin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Deletes all links on the given {@link Pin}
     *
     * @param ctx    context
     * @param pinUid the pin UID; may not be null
     */
    public void deleteLinks(final Context ctx, final String pinUid) {
        LOG.debug("Delete Links for Pin UID: {}", pinUid);

        final var pin = findPinByUID(ctx, pinUid, PinType.UNDEFINED);
        if (pin == null) {
            return;
        }

        linkService.deleteLinks(pin);
        ctx.status(HttpServletResponse.SC_NO_CONTENT);
    }

    /**
     * Returns all pins that is linked to the {@link Pin}
     *
     * @param ctx    context
     * @param pinUid the pin UID; may not be null
     */
    public void getLinks(final Context ctx, final String pinUid) {
        LOG.debug("Get Links for Pin UID: {}", pinUid);

        final var pin = findPinByUID(ctx, pinUid, PinType.UNDEFINED);
        if (pin == null) {
            return;
        }

        final var pinUids = linkService.getLinkedUIDs(pin);
        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(pinUids);
    }

    /**
     * Returns a {@link Pin} if found, otherwise {@code null}
     * and error message in {@link Context}
     *
     * @param ctx     context; may not be null
     * @param uid     UID of pin for look up; may not be null
     * @param pinType the type of Pin; may not be null
     * @return Pin if found, otherwise {@code null}
     */
    private Pin findPinByUID(final Context ctx, final String uid, final PinType pinType) {
        Pin pin = null;

        if (uid == null || uid.isBlank()) {
            setBadRequest(ctx, "No %s UID provided", pinType.getFriendlyName());
        } else {
            pin = uidRegistry.getPin(uid);
            if (pin == null) {
                setNotFound(ctx, "No %s found with UID: %s", pinType.getFriendlyName(), uid);
            }
        }
        return pin;
    }

    /**
     * Helper Enum for proper error handling
     */
    private enum PinType {
        UNDEFINED("Pin"), //
        SOURCE("Source Pin"), //
        TARGET("Target Pin");

        private final String friendlyName;

        PinType(final String friendlyName) {
            this.friendlyName = friendlyName;
        }

        public String getFriendlyName() {
            return friendlyName;
        }
    }
}
