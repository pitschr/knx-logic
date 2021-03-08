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
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-inbox-1', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-inbox-1', 1, 0, 'data');   -- connectors.id: 1
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-inbox-1', 1, 0);                 -- pins.id: 1
INSERT INTO event_keys (componentId, channel, key)                    VALUES (1, 'var', 'IN 1');

-- Component: IN 2 (id=2)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-inbox-2', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-inbox-2', 2, 0, 'data');   -- connectors.id: 2
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-inbox-2', 2, 0);                 -- pins.id: 2
INSERT INTO event_keys (componentId, channel, key)                    VALUES (2, 'var', 'IN 2');

-- Component: OUT 1 (id=3)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox-1', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox-1', 3, 0, 'data');  -- connectors.id: 3
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-outbox-1', 3, 0);                -- pins.id: 3
INSERT INTO event_keys (componentId, channel, key)                    VALUES (3, 'var', 'OUT 1');

-- Component: OUT 2 (id=4)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox-2'2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox-2', 4, 0, 'data');  -- connectors.id: 4
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-outbox-2', 4, 0);                -- pins.id: 4
INSERT INTO event_keys (componentId, channel, key)                    VALUES (4, 'var', 'OUT 2');

-- Component: And Logic (id=5)
INSERT INTO components (uid, componentType, className)           VALUES
    ('uid-component-logic', 0, 'test.components.logic.AndLogic');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-input-2',        5, 1, 'inputs'),                                                             -- connectors.id: 5
    ('uid-connector-logic-output',         5, 0, 'output'),                                                             -- connectors.id: 6
    ('uid-connector-logic-outputNegation', 5, 0, 'outputNegation');                                                     -- connectors.id: 7
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-input-1',        5, 0),                                                                             -- pins.id: 5
    ('uid-pin-logic-input-2',        5, 1),                                                                             -- pins.id: 6
    ('uid-pin-logic-output',         6, 0),                                                                             -- pins.id: 7
    ('uid-pin-logic-outputNegation', 7, 0);                                                                             -- pins.id: 8

INSERT INTO pin_links (pin1, pin2) VALUES
    (1, 5), -- pin_links.id: 1
    (2, 6), -- pin_links.id: 2
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
