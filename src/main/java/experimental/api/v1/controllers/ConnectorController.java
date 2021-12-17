package experimental.api.v1.controllers;

import li.pitschmann.knx.api.UIDRegistry;
import experimental.api.v1.json.ConnectorResponse;
import experimental.api.v1.services.ConnectorService;
import io.javalin.http.Context;
import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.exceptions.MaximumBoundException;
import li.pitschmann.knx.logic.exceptions.MinimumBoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for {@link Connector} (Input, Output)
 *
 * @author PITSCHR
 */
public final class ConnectorController {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorController.class);

    private final ConnectorService connectorService;

    public ConnectorController(final ConnectorService connectorService) {
        this.connectorService = Objects.requireNonNull(connectorService);
    }

    /**
     * Returns info about a specific connector by its {@code connectorName}
     * owned by the component
     *
     * @param ctx context
     * @param uid the connector uid
     */
    public void getOne(final Context ctx, final String uid) {
        LOG.trace("Get Connector by UID: {}", uid);

        final var connector = findConnectorByUID(ctx, uid);
        if (connector == null) {
            return;
        }

        ctx.status(HttpServletResponse.SC_OK);
        ctx.json(ConnectorResponse.from(connector));
    }

    /**
     * Adds a new pin to the connector and add the pin at the given {@code index}.
     * If index is null, then pin will be added at the last position.
     *
     * @param ctx          context
     * @param connectorUid the connector UID that should be altered
     * @param index        the index where pin should be added; may be null
     */
    public void addPin(final Context ctx,
                       final String connectorUid,
                       final @Nullable Integer index) {
        LOG.trace("Add pin for connector UID (index={}): {}", index, connectorUid);

        final var connector = findConnectorByUID(ctx, connectorUid);
        if (connector == null) {
            return;
        }

        // verify if connector is dynamic
        if (!(connector instanceof DynamicConnector)) {
            LOG.error("Connector is not dynamic: {}", connector);
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            ctx.json(Map.of(
                    "message",
                    String.format("Connector is not dynamic: %s", connector))
            );
            return;
        }
        final var dynamicConnector = (DynamicConnector) connector;

        try {
            if (index == null) {
                connectorService.addPin(dynamicConnector);
            } else {
                // index must be valid
                if (index < 0 || index >= dynamicConnector.size()) {
                    ctx.status(HttpServletResponse.SC_BAD_REQUEST);
                    ctx.json(Map.of(
                            "message",
                            String.format("Pin index is out of range: %s (min=0, max=%s)", index, dynamicConnector.size() - 1))
                    );
                    return;
                }
                connectorService.addPin(dynamicConnector, index);
            }

            ctx.status(HttpServletResponse.SC_OK);
            ctx.json(ConnectorResponse.from(connector).getPins());
        } catch (final MaximumBoundException e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Deletes the pin from connector at the given {@code index}.
     *
     * @param ctx          context
     * @param connectorUid the connector UID that should be altered
     * @param index        the index where pin should be added; may not null
     */
    public void deletePin(final Context ctx,
                          final String connectorUid,
                          final Integer index) {
        LOG.trace("Delete pin for connector UID (index={}): {}", index, connectorUid);

        final var connector = findConnectorByUID(ctx, connectorUid);
        if (connector == null) {
            return;
        }

        // verify if connector is dynamic
        if (!(connector instanceof DynamicConnector)) {
            LOG.error("Connector is not dynamic: {}", connector);
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            ctx.json(Map.of(
                    "message",
                    String.format("Connector is not dynamic: %s", connector))
            );
            return;
        }
        final var dynamicConnector = (DynamicConnector) connector;

        // index must be provided and valid
        if (index == null || index < 0 || index >= dynamicConnector.size()) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of(
                    "message",
                    String.format("Pin index is out of range: %s (min=0, max=%s)", index, dynamicConnector.size() - 1))
            );
            return;
        }

        try {
            connectorService.removePin(dynamicConnector, index);

            ctx.status(HttpServletResponse.SC_OK);
            ctx.json(ConnectorResponse.from(connector));
        } catch (final MinimumBoundException e) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of("message", e.getMessage()));
        }
    }

    /**
     * Returns a {@link Connector} if found, otherwise {@code null}
     * and error message in {@link Context}
     *
     * @param ctx context
     * @param uid UID of connector for look up
     * @return Connector if found, otherwise {@code null}
     */
    private Connector findConnectorByUID(final Context ctx, final String uid) {
        Connector connector = null;
        if (uid == null || uid.isBlank()) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            ctx.json(Map.of("message", "No connector UID provided."));
        } else {
            connector = UIDRegistry.getConnector(uid);
            if (connector == null) {
                ctx.status(HttpServletResponse.SC_NOT_FOUND);
                ctx.json(Map.of(
                        "message",
                        String.format("No connector found with UID: %s", uid))
                );
            }
        }
        return connector;
    }
}
