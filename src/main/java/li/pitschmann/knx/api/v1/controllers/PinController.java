/*
 * Copyright (C) 2022 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.api.v1.controllers;

import io.javalin.http.Context;
import li.pitschmann.knx.api.UIDRegistry;
import li.pitschmann.knx.api.v1.json.PinResponse;
import li.pitschmann.knx.api.v1.json.PinSetValueRequest;
import li.pitschmann.knx.api.v1.services.PinService;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
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
public final class PinController extends AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(PinController.class);
    private final PinService pinService;
    private final UIDRegistry uidRegistry;

    public PinController(final PinService pinService,
                         final UIDRegistry uidRegistry) {
        this.pinService = Objects.requireNonNull(pinService);
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
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

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(PinResponse.from(pin));
    }

    /**
     * Returns the value of a pin
     *
     * @param ctx    context
     * @param pinUid UID of pin to be fetched
     */
    public void getValue(final Context ctx, final String pinUid) {
        LOG.trace("Get Value for Pin by UID: {}", pinUid);

        final var pin = findPinByUID(ctx, pinUid);
        if (pin == null) {
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(PinResponse.fromWithoutPinInfo(pin));
    }

    /**
     * <p>Updates the {@link Pin}</p> (e.g. value)
     *
     * @param ctx     context
     * @param pinUid  UID of pin to be updated
     * @param request request context to update the pin
     */
    public void setValue(final Context ctx, final String pinUid, final PinSetValueRequest request) {
        LOG.trace("Set Value for Pin by UID '{}': {}", pinUid, request);

        final var pin = findPinByUID(ctx, pinUid);
        if (pin == null) {
            return;
        }

        if (pin.getDescriptor() instanceof OutputDescriptor) {
            LOG.error("Pin is declared as an Output: {}", pin);
            setForbidden(ctx,
                    "Pin is declared as an output pin, and therefore " +
                            "not suitable to set the value: %s", pin.getName()
            );
            return;
        }

        pinService.setValue(pin, request.getValue());
        ctx.status(HttpServletResponse.SC_ACCEPTED);
        ctx.json(PinResponse.fromWithoutPinInfo(pin));
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
            setBadRequest(ctx, "No pin UID provided");
        } else {
            pin = uidRegistry.getPin(uid);
            if (pin == null) {
                setNotFound(ctx, "No pin found with UID: %s", uid);
            }
        }
        return pin;
    }
}
