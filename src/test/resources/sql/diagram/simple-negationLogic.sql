--
--  .--------.      .---------.      .---------.
--  |   IN   | ---> |  LOGIC  | ---> |   OUT   |
--  `--------´      `---------´      `---------´
--

-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: Inbox (id=1)
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox', 'uid-component-inbox');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'data');                                 -- connectors.id: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-pin-inbox', 0);                        -- pins.id: 1
INSERT INTO event_keys (componentId, channel, key)               VALUES (1, 'var', 'IN');

-- Component: Outbox (id=2)
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox', 'uid-component-outbox');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'data');                                 -- connectors.id: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (2, 'uid-pin-outbox', 0);                       -- pins.id: 2
INSERT INTO event_keys (componentId, channel, key)               VALUES (2, 'var', 'OUT');

-- Component: Logic (id=3)
INSERT INTO components (componentType, className, uid)           VALUES (0, 'test.components.logic.NegationLogic', 'uid-component-logic');
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'input');                                -- connectors.id: 3
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'output');                               -- connectors.id: 4
INSERT INTO pins (connectorId, uid, index)                       VALUES (3, 'uid-pin-logic-input', 0);                  -- pins.id: 3
INSERT INTO pins (connectorId, uid, index)                       VALUES (4, 'uid-pin-logic-output', 0);                 -- pins.id: 4

-- -----------------------------------------------------------------
--  Diagram
-- -----------------------------------------------------------------
-- Diagram: Layout
INSERT INTO diagrams (name, description) VALUES ("Diagram - Negation", "Diagram Description - Negation");

-- Diagram: Components
INSERT INTO diagram_components (diagramId, componentId, position_x, position_y) VALUES (1, 1, 10, 11);
INSERT INTO diagram_components (diagramId, componentId, position_x, position_y) VALUES (1, 2, 20, 21);
INSERT INTO diagram_components (diagramId, componentId, position_x, position_y) VALUES (1, 3, 30, 31);

-- Diagram: Links
INSERT INTO diagram_links (diagramId, sourcePinId, targetPinId, svg_path_data) VALUES (1, 1, 3, 'inbox->logic[input]');
INSERT INTO diagram_links (diagramId, sourcePinId, targetPinId, svg_path_data) VALUES (1, 4, 2, 'logic[output]->outbox');
