package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.PinResponse;
import experimental.api.v1.json.PinSetValueRequest;
import experimental.api.v1.services.ConnectorService;
import experimental.api.v1.services.PinService;
import io.javalin.http.Context;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.pin.DynamicPin;
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

    public void getPin(final Context ctx, final String pinUid) {
        LOG.trace("Find Pin by UID '{}'", pinUid);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            LOG.error("No pin found for UID: {}", pinUid);
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            ctx.json(Map.of(
                    "message",
                    String.format("No pin found with UID: %s", pinUid))
            );
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(PinResponse.from(pin));
    }

    /**
     * Returns the value of a pin
     *
     * @param ctx     context
     * @param pinUid  UID of pin to be fetched
     */
    public void getValue(final Context ctx, final String pinUid) {
        LOG.trace("Get Value Pin by UID: {}", pinUid);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            LOG.error("No pin found for UID: {}", pinUid);
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            ctx.json(Map.of(
                    "message",
                    String.format("No pin found with UID: %s", pinUid))
            );
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
        LOG.trace("Set Value Pin by UID '{}': {}", pinUid, request);

        // find the pin by uid
        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            LOG.error("No pin found for UID: {}", pinUid);
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            ctx.json(Map.of(
                    "message",
                    String.format("No pin found with UID: %s", pinUid))
            );
            return;
        }

        if (pin.getDescriptor().getField().getAnnotation(Output.class) != null) {
            LOG.error("Pin is output: {}", pin);
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

    public void deletePin(final Context ctx, final String pinUid) {
        LOG.trace("Delete pin: {}", pinUid);

        final var pin = uidRegistry.findPinByUID(pinUid);
        if (pin == null) {
            LOG.error("No pin found for UID: {}", pinUid);
            ctx.status(HttpServletResponse.SC_NOT_FOUND);
            ctx.json(Map.of(
                    "message",
                    String.format("No pin found with UID: %s", pinUid))
            );
            return;
        }

        if (!(pin instanceof DynamicPin)) {
            LOG.error("Pin is not dynamic: {}", pin);
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            ctx.json(Map.of(
                    "message",
                    String.format("Pin is not dynamic: %s", pin))
            );
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
