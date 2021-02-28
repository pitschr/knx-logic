--
-- LOGIC COMPONENTS
--   ComponentType#LOGIC (ordinal = 0)
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'my.logic.MyFooBarLogic', 'uid-component-logic-foobar'); -- (taken from foobar-logic.jar)

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'inputText' ),
    (1, 0, 'outputText');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-logic-foobar#inputText',  0),
    (2, 'uid-pin-logic-foobar#outputText', 0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'foo', 'java.lang.String'),
    (2, 'bar', 'java.lang.String');
