--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Static Input
--   Outputs: N/A
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicB', 'uid-component-logic-B');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'i');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1,  'uid-pin-logic-B#i', 0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean');