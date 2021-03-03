--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Static
--   Outputs: 1x Static
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicC', 'uid-component-logic-C');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'i'),
    (1, 0, 'o');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1,  'uid-pin-logic-C#i', 0),
    (2,  'uid-pin-logic-C#o', 0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
