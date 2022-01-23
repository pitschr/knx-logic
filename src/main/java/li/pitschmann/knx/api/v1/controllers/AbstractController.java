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

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Abstract Controller containing helper methods that can be
 * consumed by all Controller implementations.
 */
abstract class AbstractController {

    protected AbstractController() {
        // NO-OP
    }

    public static void setBadRequest(final Context ctx, final String message) {
        setErrorResponse(ctx, HttpServletResponse.SC_BAD_REQUEST, message);
    }

    public static void setBadRequest(final Context ctx, final String message, final String... args) {
        setErrorResponse(ctx, HttpServletResponse.SC_BAD_REQUEST, message, args);
    }

    public static void setNotFound(final Context ctx, final String message, final String... args) {
        setErrorResponse(ctx, HttpServletResponse.SC_NOT_FOUND, message, args);
    }

    public static void setErrorResponse(final Context ctx, final int httpStatus, final String message) {
        ctx.status(httpStatus);
        ctx.json(Map.of("message", message));
    }

    public static void setErrorResponse(final Context ctx, final int httpStatus, final String message, final String... args) {
        ctx.status(httpStatus);
        ctx.json(Map.of("message", String.format(message, (Object[]) args)));
    }
}
