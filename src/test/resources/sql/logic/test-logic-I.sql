--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 2x Static
--   Outputs: 2x Static
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-I', 0, 'test.components.LogicI');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-I#inputFirst',   1, 0, 'inputFirst'  ), -- connectors.id: 1
    ('uid-connector-logic-I#inputSecond',  1, 0, 'inputSecond' ), -- connectors.id: 2
    ('uid-connector-logic-I#outputFirst',  1, 0, 'outputFirst' ), -- connectors.id: 3
    ('uid-connector-logic-I#outputSecond', 1, 0, 'outputSecond'); -- connectors.id: 4

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-I#inputFirst',   1, 0), -- pins.id: 1
    ('uid-pin-logic-I#inputSecond',  2, 0), -- pins.id: 2
    ('uid-pin-logic-I#outputFirst',  3, 0), -- pins.id: 3
    ('uid-pin-logic-I#outputSecond', 4, 0); -- pins.id: 4
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '4711',  'java.lang.Integer'),
    (2, '13',    'java.lang.Integer'),
    (3, 'Hello', 'java.lang.String' ),
    (4, 'World', 'java.lang.String' );
