--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Static
--   Outputs: 1x Static
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-C', 0, 'test.components.LogicC');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-C#i', 1, 0, 'i'),
    ('uid-connector-logic-C#o', 1, 0, 'o');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-C#i', 1, 0),
    ('uid-pin-logic-C#o', 2, 0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
