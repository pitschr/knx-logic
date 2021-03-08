--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Static Input
--   Outputs: N/A
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-B', 0, 'test.components.LogicB');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-B#i', 1, 0, 'i');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-B#i', 1,  0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean');