package experimental.api.v1.controllers;

import experimental.UidRegistry;
import experimental.api.v1.json.ConnectorResponse;
import experimental.api.v1.services.ConnectorService;
import io.javalin.http.Context;
import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Controller for {@link Connector} (Input, Output)
 *
 * @author PITSCHR
 */
public class ConnectorController {
    private static final Logger LOG = LoggerFactory.getLogger(ConnectorController.class);

    private final UidRegistry uidRegistry;
    private final ConnectorService connectorService;

    public ConnectorController(final UidRegistry uidRegistry,
                               final ConnectorService connectorService) {
        this.uidRegistry = Objects.requireNonNull(uidRegistry);
        this.connectorService = Objects.requireNonNull(connectorService);
    }

    /**
     * Returns info about a specific connector by its {@code connectorName}
     * owned by the component
     *
     * @param ctx          context
     * @param connectorUid the connector uid
     */
    public void getConnector(final Context ctx, final String connectorUid) {
        LOG.trace("Get Info for Connector UID: {}", connectorUid);
        Preconditions.checkNonNull(connectorUid, "UID for connector not provided.");

        final var connector = uidRegistry.findConnectorByUID(connectorUid);
        if (connector == null) {
            ctx.status(404);
        } else {
            ctx.status(200);
            ctx.json(ConnectorResponse.from(connector));
        }
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
        LOG.trace("Add pin at index '{}' for connector UID: {}", index, connectorUid);

        // find connector
        final var connector = uidRegistry.findConnectorByUID(connectorUid);
        if (connector == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // verify if connector is dynamic
        if (!(connector instanceof DynamicConnector)) {
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        final var dynamicConnector = (DynamicConnector) connector;

        final Pin newPin;
        if (index == null) {
            newPin = connectorService.addPin(dynamicConnector);
        } else {
            // index must be valid
            if (index < 0 || index >= dynamicConnector.size()) {
                ctx.status(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            newPin = connectorService.addPin(dynamicConnector, index);
        }
        uidRegistry.registerPin(newPin);

        ctx.status(HttpServletResponse.SC_CREATED);
        ctx.json(ConnectorResponse.from(connector).getPins());
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
        LOG.trace("Delete pin at index '{}' for connector UID: {}", index, connectorUid);

        // find connector
        final var connector = uidRegistry.findConnectorByUID(connectorUid);
        if (connector == null) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // verify if connector is dynamic
        if (!(connector instanceof DynamicConnector)) {
            ctx.status(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        final var dynamicConnector = (DynamicConnector) connector;

        // index must be provided and valid
        if (index == null || index < 0 || index >= dynamicConnector.size()) {
            ctx.status(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final var deletedPin = connectorService.removePin(dynamicConnector, index);
        uidRegistry.deregisterPin(deletedPin);

        ctx.status(HttpServletResponse.SC_NO_CONTENT);
        ctx.json(ConnectorResponse.from(connector));
    }
}
