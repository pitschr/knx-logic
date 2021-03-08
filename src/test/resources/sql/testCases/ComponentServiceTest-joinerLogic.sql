--
--  .----------.
--  |  JOINER  |
--  `----------´
--
--
-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic', 0, 'test.components.logic.JoinerLogic');

-- -----------------------------------------------------------------
--  Connectors
-- -----------------------------------------------------------------
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-input',  1, 1, 'inputs'), -- connectors.id: 1
    ('uid-connector-logic-output', 1, 0, 'output'); -- connectors.id: 2

-- -----------------------------------------------------------------
--  Pins
-- -----------------------------------------------------------------
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-input[0]', 1, 0), -- pins.id: 1
    ('uid-pin-logic-input[1]', 1, 1), -- pins.id: 2
    ('uid-pin-logic-output',   2, 0); -- pins.id: 3
