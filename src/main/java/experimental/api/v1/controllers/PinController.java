package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.PinResponse;
import experimental.api.v1.json.PinSetValueRequest;
import experimental.api.v1.services.ConnectorService;
import experimental.api.v1.services.PinService;
import io.javalin.http.Context;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
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
    private final ConnectorService connectorService;

    public PinController(final UidRegistry uidRegistry, final PinService pinService, final ConnectorService connectorService) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.pinService = Objects.requireNonNull(pinService);
        this.connectorService = Objects.requireNonNull(connectorService);
    }

    public void getValue(final Context ctx, final String pinUid) {
        LOG.trace("Find Pin by UID '{}'", pinUid);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(PinResponse.from(pin));
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
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        final var valueAsString = request.getValue();
        pinService.setValue(pin, valueAsString);

        ctx.status(HttpServletResponse.SC_ACCEPTED);
    }

    public void deletePin(final Context ctx, final String pinUid) {
        LOG.trace("Delete pin: {}", pinUid);

        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (!(pin instanceof DynamicPin)) {
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            LOG.error("Pin is not dynamic: {}", pin);
            return;
        }
        final var dynamicPin = (DynamicPin) pin;
        final var dynamicConnector = dynamicPin.getConnector();

        if (dynamicConnector == null) {
            ctx.status(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            LOG.error("No suitable dynamic connector found for pin: {}", pin);
            return;
        }

        connectorService.removePin(dynamicConnector, dynamicPin.getIndex());
        ctx.status(HttpServletResponse.SC_ACCEPTED);
    }
}
