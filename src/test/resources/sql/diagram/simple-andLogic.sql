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
-- Component: IN 1 (id=1)
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox', 'uid-component-inbox-1');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'data');                                 -- connectors.id: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-pin-inbox-1', 0);                      -- pins.id: 1
INSERT INTO event_keys (componentId, channel, key)               VALUES (1, 'var', 'IN 1');

-- Component: IN 2 (id=2)
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox', 'uid-component-inbox-2');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'data');                                 -- connectors.id: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (2, 'uid-pin-inbox-2', 0);                      -- pins.id: 2
INSERT INTO event_keys (componentId, channel, key)               VALUES (2, 'var', 'IN 2');

-- Component: OUT 1 (id=3)
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox', 'uid-component-outbox-1');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'data');                                 -- connectors.id: 3
INSERT INTO pins (connectorId, uid, index)                       VALUES (3, 'uid-pin-outbox-1', 0);                     -- pins.id: 3
INSERT INTO event_keys (componentId, channel, key)               VALUES (3, 'var', 'OUT 1');

-- Component: OUT 2 (id=4)
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox', 'uid-component-outbox-2');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (4, 0, 'data');                                 -- connectors.id: 4
INSERT INTO pins (connectorId, uid, index)                       VALUES (4, 'uid-pin-outbox-2', 0);                     -- pins.id: 4
INSERT INTO event_keys (componentId, channel, key)               VALUES (4, 'var', 'OUT 2');

-- Component: And Logic (id=5)
INSERT INTO components (componentType, className, uid)           VALUES
    (0, 'test.components.logic.AndLogic', 'uid-component-logic');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (5, 1, 'inputs'),                               -- connectors.id: 5
    (5, 0, 'output'),                               -- connectors.id: 6
    (5, 0, 'outputNegation');                       -- connectors.id: 7
INSERT INTO pins (connectorId, uid, index) VALUES
    (5, 'uid-pin-logic-input-1', 0),                -- pins.id: 5
    (5, 'uid-pin-logic-input-2', 1),                -- pins.id: 6
    (6, 'uid-pin-logic-output', 0),                 -- pins.id: 7
    (7, 'uid-pin-logic-outputNegation', 0);         -- pins.id: 8

INSERT INTO pin_links (pin1, pin2) VALUES
    (1, 5), -- pin_links.id: 1
    (1, 2), -- pin_links.id: 2
    (3, 7), -- pin_links.id: 3
    (4, 8); -- pin_links.id: 4

-- -----------------------------------------------------------------
--  Diagram
-- -----------------------------------------------------------------
-- Diagram: Layout
INSERT INTO diagrams (name, description) VALUES
    ('Diagram - And', 'Diagram Description - And');

-- Diagram: Components
INSERT INTO diagram_components (diagramId, componentId, positionX, positionY) VALUES
    (1, 1, 10, 11),
    (1, 2, 20, 21),
    (1, 3, 30, 31),
    (1, 4, 40, 41),
    (1, 5, 50, 51);

-- Diagram: Links
INSERT INTO diagram_links (diagramId, pinLinkId, svgPath) VALUES
    (1, 1, 'in 1->logic.input[0]'),
    (1, 2, 'in 2->logic.input[1]'),
    (1, 3, 'logic.output->out 1'),
    (1, 4, 'logic.outputNegation->out 2');
