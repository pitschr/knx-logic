package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.PinResponse;
import experimental.api.v1.json.PinSetValueRequest;
import experimental.api.v1.services.PinService;
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
    private final PinService pinService;

    public PinController(final UidRegistry uidRegistry, final PinService pinService) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.pinService = Objects.requireNonNull(pinService);
    }

    /**
     * <p>Updates the {@link Pin}</p> (e.g. value)
     *
     * @param ctx     context
     * @param pinUid  UID of pin to be updated
     * @param request request context to update the pin
     */
    public void setValue(final Context ctx, final String pinUid, final PinSetValueRequest request) {
        LOG.trace("Update Pin by UID '{}': {}", pinUid, request);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            ctx.status(404);
            return;
        }

        final var valueAsString = request.getValue();
        pinService.setValue(pin, valueAsString);

        ctx.status(204);
    }

    private PinResponse toPinResponse(final Pin pin) {
        final var pinResponse = new PinResponse();
        pinResponse.setUid(pin.getUid().toString());
        pinResponse.setValue(String.valueOf(pin.getValue()));
        return pinResponse;
    }

}
