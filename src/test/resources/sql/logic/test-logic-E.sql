--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Dynamic
--   Outputs: 1x Dynamic
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicE', 'uid-component-logic-E');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 1, 'i'),
    (1, 1, 'o');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1,  'uid-pin-logic-E#i[0]', 0),
    (1,  'uid-pin-logic-E#i[1]', 1),
    (2,  'uid-pin-logic-E#o[0]', 0),
    (2,  'uid-pin-logic-E#o[1]', 1);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean'),
    (3, '0', 'java.lang.Boolean'),
    (4, '1', 'java.lang.Boolean');
