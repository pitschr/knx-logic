--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Dynamic
--   Outputs: N/A
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicD', 'uid-component-logic-D');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 1, 'i');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1,  'uid-pin-logic-D#i[0]', 0),
    (1,  'uid-pin-logic-D#i[1]', 1);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
