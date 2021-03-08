--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 8x Dynamic
--   Outputs: 1x Dynamic
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-H', 0, 'test.components.LogicH');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-H#booleans', 1, 1, 'booleans'), -- connectors.id: 1
    ('uid-connector-logic-H#bytes',    1, 1, 'bytes'   ), -- connectors.id: 2
    ('uid-connector-logic-H#chars',    1, 1, 'chars'   ), -- connectors.id: 3
    ('uid-connector-logic-H#doubles',  1, 1, 'doubles' ), -- connectors.id: 4
    ('uid-connector-logic-H#floats',   1, 1, 'floats'  ), -- connectors.id: 5
    ('uid-connector-logic-H#integers', 1, 1, 'integers'), -- connectors.id: 6
    ('uid-connector-logic-H#longs',    1, 1, 'longs'   ), -- connectors.id: 7
    ('uid-connector-logic-H#shorts',   1, 1, 'shorts'  ), -- connectors.id: 8
    ('uid-connector-logic-H#strings',  1, 1, 'strings' ); -- connectors.id: 9

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-H#booleans[0]', 1, 0), -- pins.id: 1
    ('uid-pin-logic-H#booleans[1]', 1, 1), -- pins.id: 2
    ('uid-pin-logic-H#bytes[0]',    2, 0), -- pins.id: 3
    ('uid-pin-logic-H#bytes[1]',    2, 1), -- pins.id: 4
    ('uid-pin-logic-H#chars[0]',    3, 0), -- pins.id: 5
    ('uid-pin-logic-H#chars[1]',    3, 1), -- pins.id: 6
    ('uid-pin-logic-H#doubles[0]',  4, 0), -- pins.id: 7
    ('uid-pin-logic-H#doubles[1]',  4, 1), -- pins.id: 8
    ('uid-pin-logic-H#floats[0]',   5, 0), -- pins.id: 9
    ('uid-pin-logic-H#floats[1]',   5, 1), -- pins.id: 10
    ('uid-pin-logic-H#integers[0]', 6, 0), -- pins.id: 11
    ('uid-pin-logic-H#integers[1]', 6, 1), -- pins.id: 12
    ('uid-pin-logic-H#longs[0]',    7, 0), -- pins.id: 13
    ('uid-pin-logic-H#longs[1]',    7, 1), -- pins.id: 14
    ('uid-pin-logic-H#shorts[0]',   8, 0), -- pins.id: 15
    ('uid-pin-logic-H#shorts[1]',   8, 1), -- pins.id: 16
    ('uid-pin-logic-H#strings[0]',  9, 0), -- pins.id: 17
    ('uid-pin-logic-H#strings[1]',  9, 1); -- pins.id: 18
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
