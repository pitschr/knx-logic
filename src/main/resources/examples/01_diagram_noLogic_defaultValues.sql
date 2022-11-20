--
--  .------.      .-------.
--  |  IN  | ---> |  OUT  |
--  `------´      `-------´
--

-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: IN (id=3)
INSERT INTO components (id, uid, componentType, className)                VALUES (3, 'nologic-defaultValues-uid-component-in', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (3, 'nologic-defaultValues-uid-connector-in', 3, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (3, 'nologic-defaultValues-uid-pin-in', 3, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (3, 3, 'var', 'IN-DEFAULTVALUE');

-- Component: OUT (id=4)
INSERT INTO components (id, uid, componentType, className)                VALUES (4, 'nologic-defaultValues-uid-component-out', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (4, 'nologic-defaultValues-uid-connector-out', 4, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (4, 'nologic-defaultValues-uid-pin-out', 4, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (4, 4, 'var', 'OUT-DEFAULTVALUE');

INSERT INTO pin_links (id, pin1, pin2) VALUES
    (2, 3, 4);

INSERT INTO pin_values (id, pinId, value, valueType) VALUES
    (1, 3, 'in-default', 'java.lang.String'),
    (2, 4, 'out-default', 'java.lang.String');

-- -----------------------------------------------------------------
--  Diagram
-- -----------------------------------------------------------------
-- Diagram: Layout
INSERT INTO diagrams (id, uid, name, description) VALUES
    (2, 'nologic-defaultValues-uid-diagram', 'Diagram - NoLogic (with Default Values)', 'Diagram Description - NoLogic (with Default Values)');

-- Diagram: Components
INSERT INTO diagram_components (diagramId, componentId, positionX, positionY) VALUES
    (2, 3, 30, 31),
    (2, 4, 40, 41);

-- Diagram: Links
INSERT INTO diagram_links (diagramId, pinLinkId, svgPath) VALUES
    (2, 2, 'in(defaultValues)->out(defaultValues)');
