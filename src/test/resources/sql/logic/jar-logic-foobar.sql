--
-- LOGIC COMPONENTS
--   ComponentType#LOGIC (ordinal = 0)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-foobar', 0, 'my.logic.MyFooBarLogic'); -- (taken from foobar-logic.jar)

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-foobar#inputText',  1, 0, 'inputText' ),
    ('uid-connector-logic-foobar#outputText', 1, 0, 'outputText');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-foobar#inputText',  1, 0),
    ('uid-pin-logic-foobar#outputText', 2, 0);
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'foo', 'java.lang.String'),
    (2, 'bar', 'java.lang.String');
