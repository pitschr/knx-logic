--
--  .------.      .-------.
--  |  IN  | ---> |  OUT  |
--  `------´      `-------´
--

-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: IN (id=1)
INSERT INTO components (id, uid, componentType, className)                VALUES (1, 'nologic-uid-component-in', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (1, 'nologic-uid-connector-in', 1, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (1, 'nologic-uid-pin-in', 1, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (1, 1, 'var', 'IN');

-- Component: OUT (id=2)
INSERT INTO components (id, uid, componentType, className)                VALUES (2, 'nologic-uid-component-out', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (2, 'nologic-uid-connector-out', 2, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (2, 'nologic-uid-pin-outbox-1', 2, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (2, 2, 'var', 'OUT');

INSERT INTO pin_links (id, pin1, pin2) VALUES (1, 1, 2);

-- -----------------------------------------------------------------
--  Diagram
-- -----------------------------------------------------------------
-- Diagram: Layout
INSERT INTO diagrams (id, uid, name, description) VALUES
    (1, 'nologic-uid-diagram', 'Diagram - NoLogic', 'Diagram Description - NoLogic');

-- Diagram: Components
INSERT INTO diagram_components (diagramId, componentId, positionX, positionY) VALUES
    (1, 1, 10, 11),
    (1, 2, 20, 21);

-- Diagram: Links
INSERT INTO diagram_links (diagramId, pinLinkId, svgPath) VALUES
    (1, 1, 'in->out');
