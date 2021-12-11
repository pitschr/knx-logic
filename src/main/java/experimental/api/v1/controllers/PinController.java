package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.PinResponse;
import experimental.api.v1.json.PinSetValueRequest;
import experimental.api.v1.services.PinService;
import io.javalin.http.Context;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for {@link Pin} management
 *
 * @author PITSCHR
 */
public final class PinController {
    private static final Logger LOG = LoggerFactory.getLogger(PinController.class);
    private final PinService pinService;

    public PinController(final PinService pinService) {
        this.pinService = Objects.requireNonNull(pinService);
    }

    /**
     * Returns response about a specific pin by its {@code uid}
     *
     * @param ctx context
     * @param uid the pin uid
     */
    public void getOne(final Context ctx, final String uid) {
        LOG.trace("Get Pin by UID: {}", uid);

        final var pin = findPinByUID(ctx, uid);
        if (pin == null) {
            return;
        }

        ctx.status(HttpServletResponse.SC_NO_CONTENT);
        ctx.json(PinResponse.from(pin));
    }

    /**
     * Returns the value of a pin
     *
     * @param ctx    context
     * @param pinUid UID of pin to be fetched
     */
    public void getValue(final Context ctx, final String pinUid) {
        LOG.trace("Get Value Pin by UID: {}", pinUid);

        final var pin = findPinByUID(ctx, pinUid);
        if (pin == null) {
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(Map.of("value", String.valueOf(pin.getValue())));
    }

    /**
     * <p>Updates the {@link Pin}</p> (e.g. value)
     *
     * @param ctx     context
     * @param pinUid  UID of pin to be updated
     * @param request request context to update the pin
     */
    public void setValue(final Context ctx, final String pinUid, final PinSetValueRequest request) {
        LOG.trace("Set Value Pin for UID '{}': {}", pinUid, request);

        final var pin = findPinByUID(ctx, pinUid);
        if (pin == null) {
            return;
        }

        if (pin.getDescriptor() instanceof OutputDescriptor) {
            LOG.error("Pin is declared as an Output: {}", pin);
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            ctx.json(Map.of(
                    "message",
                    String.format("Pin is declared as an output pin, and therefore not suitable to set the value: %s", pin))
            );
            return;
        }

        final var valueAsString = request.getValue();
        pinService.setValue(pin, valueAsString);

        ctx.status(HttpServletResponse.SC_ACCEPTED);
    }

    /**
     * Returns a {@link Pin} if found, otherwise {@code null}
     * and error message in {@link Context}
     *
     * @param ctx context
     * @param uid UID of pin for look up
     * @return Pin if found, otherwise {@code null}
     */
    private Pin findPinByUID(final Context ctx, final String uid) {
        Pin pin = null;
        if (uid == null || uid.isBlank()) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of("message", "No pin UID provided."));
        } else {
            pin = UidRegistry.findPinByUID(uid);
            if (pin == null) {
                ctx.status(HttpServletResponse.SC_NOT_FOUND);
                ctx.json(Map.of(
                        "message",
                        String.format("No pin found with UID: %s", uid))
                );
            }
        }
        return pin;
    }
}
