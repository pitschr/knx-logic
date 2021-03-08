--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 2x Dynamic
--   Outputs: 2x Dynamic
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-J', 0, 'test.components.LogicJ');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-J#inputFirst',   1, 1, 'inputFirst'  ), -- connectors.id: 1
    ('uid-connector-logic-J#inputSecond',  1, 1, 'inputSecond' ), -- connectors.id: 2
    ('uid-connector-logic-J#outputFirst',  1, 1, 'outputFirst' ), -- connectors.id: 3
    ('uid-connector-logic-J#outputSecond', 1, 1, 'outputSecond'); -- connectors.id: 4

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-J#inputFirst[0]',   1, 0), -- pins.id: 1
    ('uid-pin-logic-J#inputFirst[1]',   1, 1), -- pins.id: 2
    ('uid-pin-logic-J#inputFirst[2]',   1, 2), -- pins.id: 3
    ('uid-pin-logic-J#inputSecond[0]',  2, 0), -- pins.id: 4
    ('uid-pin-logic-J#inputSecond[1]',  2, 1), -- pins.id: 5
    ('uid-pin-logic-J#outputFirst[0]',  3, 0), -- pins.id: 6
    ('uid-pin-logic-J#outputFirst[1]',  3, 1), -- pins.id: 7
    ('uid-pin-logic-J#outputFirst[2]',  3, 2), -- pins.id: 8
    ('uid-pin-logic-J#outputFirst[3]',  3, 3), -- pins.id: 9
    ('uid-pin-logic-J#outputSecond[0]', 4, 0), -- pins.id: 10
    ('uid-pin-logic-J#outputSecond[1]', 4, 1); -- pins.id: 11
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
