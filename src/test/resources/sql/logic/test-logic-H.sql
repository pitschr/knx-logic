--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 8x Dynamic
--   Outputs: 1x Dynamic
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicH', 'uid-component-logic-H');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 1, 'booleans'), -- connectors.id: 1
    (1, 1, 'bytes'   ), -- connectors.id: 2
    (1, 1, 'chars'   ), -- connectors.id: 3
    (1, 1, 'doubles' ), -- connectors.id: 4
    (1, 1, 'floats'  ), -- connectors.id: 5
    (1, 1, 'integers'), -- connectors.id: 6
    (1, 1, 'longs'   ), -- connectors.id: 7
    (1, 1, 'shorts'  ), -- connectors.id: 8
    (1, 1, 'strings' ); -- connectors.id: 9

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-logic-H#booleans[0]', 0), -- pins.id: 1
    (1, 'uid-pin-logic-H#booleans[1]', 1), -- pins.id: 2
    (2, 'uid-pin-logic-H#bytes[0]',    0), -- pins.id: 3
    (2, 'uid-pin-logic-H#bytes[1]',    1), -- pins.id: 4
    (3, 'uid-pin-logic-H#chars[0]',    0), -- pins.id: 5
    (3, 'uid-pin-logic-H#chars[1]',    1), -- pins.id: 6
    (4, 'uid-pin-logic-H#doubles[0]',  0), -- pins.id: 7
    (4, 'uid-pin-logic-H#doubles[1]',  1), -- pins.id: 8
    (5, 'uid-pin-logic-H#floats[0]',   0), -- pins.id: 9
    (5, 'uid-pin-logic-H#floats[1]',   1), -- pins.id: 10
    (6, 'uid-pin-logic-H#integers[0]', 0), -- pins.id: 11
    (6, 'uid-pin-logic-H#integers[1]', 1), -- pins.id: 12
    (7, 'uid-pin-logic-H#longs[0]',    0), -- pins.id: 13
    (7, 'uid-pin-logic-H#longs[1]',    1), -- pins.id: 14
    (8, 'uid-pin-logic-H#shorts[0]',   0), -- pins.id: 15
    (8, 'uid-pin-logic-H#shorts[1]',   1), -- pins.id: 16
    (9, 'uid-pin-logic-H#strings[0]',  0), -- pins.id: 17
    (9, 'uid-pin-logic-H#strings[1]',  1); -- pins.id: 18
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1,  '0',                       'java.lang.Boolean'  ),
    (2,  '1',                       'java.lang.Boolean'  ),
    (3,  '-128',                    'java.lang.Byte'     ),
    (4,  '127',                     'java.lang.Byte'     ),
    (5,  '\u0000',                  'java.lang.Character'),
    (6,  '\uFFFF',                  'java.lang.Character'),
    (7,  '4.9e-324',                'java.lang.Double'   ),
    (8,  '1.7976931348623157e+308', 'java.lang.Double'   ),
    (9,  '1.4e-45f',                'java.lang.Float'    ),
    (10, '3.4028235e+38f',          'java.lang.Float'    ),
    (11, '-2147483648',             'java.lang.Integer'  ),
    (12, '2147483647',              'java.lang.Integer'  ),
    (13, '-9223372036854775808',    'java.lang.Long'     ),
    (14, '9223372036854775807',     'java.lang.Long'     ),
    (15, '-32768',                  'java.lang.Short'    ),
    (16, '32767',                   'java.lang.Short'    ),
    (17, 'Lorem Ipsum',             'java.lang.String'   ),
    (18, 'Dolor Sit Amet',          'java.lang.String'   );
