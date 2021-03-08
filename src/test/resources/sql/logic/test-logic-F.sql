--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Static, 1x Dynamic
--   Outputs: 1x Static, 1x Dynamic
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-F', 0, 'test.components.LogicF');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-F#input',   1, 0, 'input'),    -- connectors.id: 1
    ('uid-connector-logic-F#inputs',  1, 1, 'inputs'),   -- connectors.id: 2
    ('uid-connector-logic-F#output',  1, 0, 'output'),   -- connectors.id: 3
    ('uid-connector-logic-F#outputs', 1, 1, 'outputs');  -- connectors.id: 4

--
-- PINS
--
-- Logic F
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-F#input',      1, 0),  -- pins.id: 1
    ('uid-pin-logic-F#inputs[0]',  2, 0),  -- pins.id: 2
    ('uid-pin-logic-F#inputs[1]',  2, 1),  -- pins.id: 3
    ('uid-pin-logic-F#output',     3, 0),  -- pins.id: 4
    ('uid-pin-logic-F#outputs[0]', 4, 0),  -- pins.id: 5
    ('uid-pin-logic-F#outputs[1]', 4, 1),  -- pins.id: 6
    ('uid-pin-logic-F#outputs[2]', 4, 2);  -- pins.id: 7
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'Static Input',      'java.lang.String'),
    (2, 'Static Inputs[0]',  'java.lang.String'),
    (3, 'Static Inputs[1]',  'java.lang.String'),
    (4, 'Static Output',     'java.lang.String'),
    (5, 'Static Outputs[0]', 'java.lang.String'),
    (6, 'Static Outputs[1]', 'java.lang.String'),
    (7, 'Static Outputs[2]', 'java.lang.String');
