--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Dynamic
--   Outputs: 1x Dynamic
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-E', 0, 'test.components.LogicE');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-E#i', 1, 1, 'i'),
    ('uid-connector-logic-E#o', 1, 1, 'o');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-E#i[0]', 1, 0),
    ('uid-pin-logic-E#i[1]', 1, 1),
    ('uid-pin-logic-E#o[0]', 2, 0),
    ('uid-pin-logic-E#o[1]', 2, 1);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean'),
    (3, '0', 'java.lang.Boolean'),
    (4, '1', 'java.lang.Boolean');
