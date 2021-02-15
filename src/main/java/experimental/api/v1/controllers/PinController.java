package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.PinRequest;
import experimental.api.v1.json.PinResponse;
import io.javalin.http.Context;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Controller for {@link Pin} management
 *
 * @author PITSCHR
 */
public class PinController {
    private static final Logger LOG = LoggerFactory.getLogger(PinController.class);

    private final UidRegistry uidRegistry;

    public PinController(final UidRegistry uidRegistry) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
    }

    /**
     * Returns data about individual {@link Pin}
     *
     * @param ctx    context
     * @param pinUid UID of pin to be returned as JSON representation
     */
    public void getPin(final Context ctx, final String pinUid) {
        LOG.trace("Get Info for Pin by UID: {}", pinUid);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            ctx.status(404);
            return;
        }

        // TODO: get pin response

        final var response = new PinResponse();
        ctx.status(200);
        ctx.json(response);
    }

    /**
     * <p>Updates the {@link Pin}</p> (e.g. value)
     *
     * @param ctx     context
     * @param pinUid  UID of pin to be updated
     * @param request request context to update the pin
     */
    public void updatePin(final Context ctx, final String pinUid, final PinRequest request) {
        LOG.trace("Update Pin by UID '{}': {}", pinUid, request);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            ctx.status(404);
            return;
        }

        // TODO: update the pin

        final var response = new PinResponse();
        ctx.status(200);
        ctx.json(response);
    }

}
