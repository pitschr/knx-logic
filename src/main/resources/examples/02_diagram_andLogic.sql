--
--  .--------.      .---------.      .--------------.
--  |  IN 1  | ---> |         | ---> |  OUT 1 (AND) |
--  `--------´      |   AND   |      `--------------´
--  .--------.      |  LOGIC  |      .--------------.
--  |  IN 2  | ---> |         | ---> | OUT 2 (NAND) |
--  `--------´      `--------´       `--------------´
--

-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: IN 1
INSERT INTO components (id, uid, componentType, className)                VALUES (10, 'andLogic-uid-component-inbox-1', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (10, 'andLogic-uid-connector-inbox-1', 10, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (10, 'andLogic-uid-pin-inbox-1', 10, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (10, 10, 'var', 'IN 1');

-- Component: IN 2
INSERT INTO components (id, uid, componentType, className)                VALUES (11, 'andLogic-uid-component-inbox-2', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (11, 'andLogic-uid-connector-inbox-2', 11, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (11, 'andLogic-uid-pin-inbox-2', 11, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (11, 11, 'var', 'IN 2');

-- Component: OUT 1
INSERT INTO components (id, uid, componentType, className)                VALUES (12, 'andLogic-uid-component-outbox-1', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (12, 'andLogic-uid-connector-outbox-1', 12, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (12, 'andLogic-uid-pin-outbox-1', 12, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (12, 12, 'var', 'OUT 1');

-- Component: OUT 2
INSERT INTO components (id, uid, componentType, className)                VALUES (13, 'andLogic-uid-component-outbox-2', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES (13, 'andLogic-uid-connector-outbox-2', 13, 0, 'data');
INSERT INTO pins       (id, uid, connectorId, index)                      VALUES (13, 'andLogic-uid-pin-outbox-2', 13, 0);
INSERT INTO event_keys (id, componentId, channel, key)                    VALUES (13, 13, 'var', 'OUT 2');

-- Component: And Logic
INSERT INTO components (id, uid, componentType, className)                VALUES
    (14, 'andLogic-uid-component-logic', 0, 'li.pitschmann.knx.logic.components.bitwise.And');
INSERT INTO connectors (id, uid, componentId, bindingType, connectorName) VALUES
    (14, 'andLogic-uid-connector-logic-input',          14, 1, 'inputs'),
    (15, 'andLogic-uid-connector-logic-output',         14, 0, 'output'),
    (16, 'andLogic-uid-connector-logic-outputNegation', 14, 0, 'outputNegation');
INSERT INTO pins (id, uid, connectorId, index) VALUES
    (14, 'andLogic-uid-pin-logic-input-1',        14, 0),
    (15, 'andLogic-uid-pin-logic-input-2',        14, 1),
    (16, 'andLogic-uid-pin-logic-output',         15, 0),
    (17, 'andLogic-uid-pin-logic-outputNegation', 16, 0);

INSERT INTO pin_links (id, pin1, pin2) VALUES
    (10, 10, 14), -- IN 1 -> AndLogic Input [0]
    (11, 11, 15), -- IN 2 -> AndLogic Input[1]
    (12, 12, 16), -- AndLogic OUTPUT -> OUT 1
    (13, 13, 17); -- AndLogic OUTPUT NEGATION -> OUT 2

-- -----------------------------------------------------------------
--  Diagram
-- -----------------------------------------------------------------
-- Diagram: Layout
INSERT INTO diagrams (id, uid, name, description) VALUES
    (3, 'andLogic-uid-diagram-and', 'Diagram - And Logic', 'Diagram Description - And Logic');

-- Diagram: Components
INSERT INTO diagram_components (diagramId, componentId, positionX, positionY) VALUES
    (3, 10, 210, 211),
    (3, 11, 220, 221),
    (3, 12, 230, 231),
    (3, 13, 240, 241),
    (3, 14, 250, 251);

-- Diagram: Links
INSERT INTO diagram_links (diagramId, pinLinkId, svgPath) VALUES
    (3, 10, 'in 1->logic.input[0]'),
    (3, 11, 'in 2->logic.input[1]'),
    (3, 12, 'logic.output->out 1'),
    (3, 13, 'logic.outputNegation->out 2');
