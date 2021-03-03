--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 2x Static
--   Outputs: 2x Static
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicI', 'uid-component-logic-I');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'inputFirst'  ), -- connectors.id: 1
    (1, 0, 'inputSecond' ), -- connectors.id: 2
    (1, 0, 'outputFirst' ), -- connectors.id: 3
    (1, 0, 'outputSecond'); -- connectors.id: 4

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-logic-I#inputFirst',   0), -- pins.id: 1
    (2, 'uid-pin-logic-I#inputSecond',  0), -- pins.id: 2
    (3, 'uid-pin-logic-I#outputFirst',  0), -- pins.id: 3
    (4, 'uid-pin-logic-I#outputSecond', 0); -- pins.id: 4
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '4711',  'java.lang.Integer'),
    (2, '13',    'java.lang.Integer'),
    (3, 'Hello', 'java.lang.String' ),
    (4, 'World', 'java.lang.String' );
