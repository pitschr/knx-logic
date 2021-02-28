--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 2x Dynamic
--   Outputs: 2x Dynamic
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicJ', 'uid-component-logic-J');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 1, 'inputFirst'  ), -- connectors.id: 1
    (1, 1, 'inputSecond' ), -- connectors.id: 2
    (1, 1, 'outputFirst' ), -- connectors.id: 3
    (1, 1, 'outputSecond'); -- connectors.id: 4

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-logic-J#inputFirst[0]',   0), -- pins.id: 1
    (1, 'uid-pin-logic-J#inputFirst[1]',   1), -- pins.id: 2
    (1, 'uid-pin-logic-J#inputFirst[2]',   2), -- pins.id: 3
    (2, 'uid-pin-logic-J#inputSecond[0]',  0), -- pins.id: 4
    (2, 'uid-pin-logic-J#inputSecond[1]',  1), -- pins.id: 5
    (3, 'uid-pin-logic-J#outputFirst[0]',  0), -- pins.id: 6
    (3, 'uid-pin-logic-J#outputFirst[1]',  1), -- pins.id: 7
    (3, 'uid-pin-logic-J#outputFirst[2]',  2), -- pins.id: 8
    (3, 'uid-pin-logic-J#outputFirst[3]',  3), -- pins.id: 9
    (4, 'uid-pin-logic-J#outputSecond[0]', 0), -- pins.id: 10
    (4, 'uid-pin-logic-J#outputSecond[1]', 1); -- pins.id: 11
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1,  '1024',  'java.lang.Integer'),
    (2,  '4201',  'java.lang.Integer'),
    (3,  '2014',  'java.lang.Integer'),
    (4,  '17',    'java.lang.Integer'),
    (5,  '71',    'java.lang.Integer'),
    (6,  'Hello', 'java.lang.String' ),
    (7,  'olleH', 'java.lang.String' ),
    (8,  'lloeH', 'java.lang.String' ),
    (9,  'Hoell', 'java.lang.String' ),
    (10, 'World', 'java.lang.String' ),
    (11, 'dlroW', 'java.lang.String' );
