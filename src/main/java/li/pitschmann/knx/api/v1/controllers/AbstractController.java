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
import li.pitschmann.knx.core.utils.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Abstract Controller containing helper methods that can be
 * consumed by all Controller implementations.
 */
abstract class AbstractController {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentController.class);

    protected AbstractController() {
        // NO-OP
    }

    /**
     * Sets {@link Context} with {@link HttpServletResponse#SC_BAD_REQUEST} and
     * customized message
     *
     * @param ctx     the context to be updated
     * @param message the customized message
     * @param args    arguments for customized message
     */
    protected static void setBadRequest(final Context ctx, final String message, final Object... args) {
        setErrorResponse(ctx, HttpServletResponse.SC_BAD_REQUEST, message, args);
    }

    /**
     * Sets {@link Context} with {@link HttpServletResponse#SC_NOT_FOUND} and
     * customized message
     *
     * @param ctx     the context to be updated
     * @param message the customized message
     * @param args    arguments for customized message
     */
    protected static void setNotFound(final Context ctx, final String message, final Object... args) {
        setErrorResponse(ctx, HttpServletResponse.SC_NOT_FOUND, message, args);
    }

    /**
     * Sets {@link Context} with {@link HttpServletResponse#SC_FORBIDDEN} and
     * customized message
     *
     * @param ctx     the context to be updated
     * @param message the customized message
     * @param args    arguments for customized message
     */
    protected static void setForbidden(final Context ctx, final String message, final Object... args) {
        setErrorResponse(ctx, HttpServletResponse.SC_FORBIDDEN, message, args);
    }

    /**
     * Generic Error Message
     */
    private static void setErrorResponse(final Context ctx, final int httpStatus, final String message, final Object... args) {
        final String finalMessage;
        if (args == null || args.length == 0) {
            finalMessage = message;
        } else {
            finalMessage = String.format(message, args);
        }

        ctx.status(httpStatus);
        ctx.json(Map.of("message", finalMessage));
    }

    /**
     * Returns a range of {@code T} elements from list.
     * May be limited using {@code start} and {@code limit} request parameters.
     *
     * @param ctx  the context from Javalin
     * @param list the list that may be limited
     * @param <T>  the type of list value to be limited
     * @return a new list of elements from {@link Collection}
     */
    protected final <T> List<T> limitAndGetAsList(final Context ctx, final Collection<T> list) {
        final var start = getIntParameter(ctx, "start", 0);
        final var limit = getIntParameter(ctx, "limit", Integer.MAX_VALUE);
        Preconditions.checkArgument(start >= 0, "Start should be 0 or greater: {}", start);
        Preconditions.checkArgument(limit >= 0, "Limit should be 0 or greater: {}", limit);

        if (start == 0 && limit == Integer.MAX_VALUE) {
            LOG.trace("No range defined.");
            // no limit
            return List.copyOf(list);
        } else {
            LOG.trace("Range defined: start={}, limit={}", start, limit);
            return list.stream().skip(start).limit(limit).collect(Collectors.toUnmodifiableList());
        }
    }

    /**
     * Returns the value of {@code parameterName} from query string. If not present,
     * then return the {@code defaultValue}
     *
     * @param ctx           the context from Javalin
     * @param parameterName the name of parameter from query string map
     * @param defaultValue  the value; may be null
     * @return the value from query string, if not present then {@code defaultValue}
     */
    private int getIntParameter(final Context ctx, final String parameterName, final int defaultValue) {
        final var strValues = ctx.queryParamMap().get(parameterName);
        if (strValues == null || strValues.isEmpty()) {
            return defaultValue;
        } else {
            return Integer.parseInt(strValues.get(0));
        }
    }
}
