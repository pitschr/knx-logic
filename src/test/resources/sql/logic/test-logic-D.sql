--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 1x Dynamic
--   Outputs: N/A
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-D', 0, 'test.components.LogicD');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-D#i', 1, 1, 'i');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-D#i[0]', 1, 0),
    ('uid-pin-logic-D#i[1]', 1, 1);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
