--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 8x Static
--   Outputs: 9x Static
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-G', 0, 'test.components.LogicG');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-G#inputBooleanPrimitive',   1, 0, 'inputBooleanPrimitive'  ), -- connectors.id: 1
    ('uid-connector-logic-G#inputBytePrimitive',      1, 0, 'inputBytePrimitive'     ), -- connectors.id: 2
    ('uid-connector-logic-G#inputCharacterPrimitive', 1, 0, 'inputCharacterPrimitive'), -- connectors.id: 3
    ('uid-connector-logic-G#inputDoublePrimitive',    1, 0, 'inputDoublePrimitive'   ), -- connectors.id: 4
    ('uid-connector-logic-G#inputFloatPrimitive',     1, 0, 'inputFloatPrimitive'    ), -- connectors.id: 5
    ('uid-connector-logic-G#inputIntegerPrimitive',   1, 0, 'inputIntegerPrimitive'  ), -- connectors.id: 6
    ('uid-connector-logic-G#inputLongPrimitive',      1, 0, 'inputLongPrimitive'     ), -- connectors.id: 7
    ('uid-connector-logic-G#inputShortPrimitive',     1, 0, 'inputShortPrimitive'    ), -- connectors.id: 8
    ('uid-connector-logic-G#outputBooleanObject',     1, 0, 'outputBooleanObject'    ), -- connectors.id: 9
    ('uid-connector-logic-G#outputByteObject',        1, 0, 'outputByteObject'       ), -- connectors.id: 10
    ('uid-connector-logic-G#outputCharacterObject',   1, 0, 'outputCharacterObject'  ), -- connectors.id: 11
    ('uid-connector-logic-G#outputDoubleObject',      1, 0, 'outputDoubleObject'     ), -- connectors.id: 12
    ('uid-connector-logic-G#outputFloatObject',       1, 0, 'outputFloatObject'      ), -- connectors.id: 13
    ('uid-connector-logic-G#outputIntegerObject',     1, 0, 'outputIntegerObject'    ), -- connectors.id: 14
    ('uid-connector-logic-G#outputLongObject',        1, 0, 'outputLongObject'       ), -- connectors.id: 15
    ('uid-connector-logic-G#outputShortObject',       1, 0, 'outputShortObject'      ), -- connectors.id: 16
    ('uid-connector-logic-G#outputString',            1, 0, 'outputString'           ); -- connectors.id: 17

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-G#inputBooleanPrimitive',   1,  0), -- pins.id: 1
    ('uid-pin-logic-G#inputBytePrimitive',      2,  0), -- pins.id: 2
    ('uid-pin-logic-G#inputCharacterPrimitive', 3,  0), -- pins.id: 3
    ('uid-pin-logic-G#inputDoublePrimitive',    4,  0), -- pins.id: 4
    ('uid-pin-logic-G#inputFloatPrimitive',     5,  0), -- pins.id: 5
    ('uid-pin-logic-G#inputIntegerPrimitive',   6,  0), -- pins.id: 6
    ('uid-pin-logic-G#inputLongPrimitive',      7,  0), -- pins.id: 7
    ('uid-pin-logic-G#inputShortPrimitive',     8,  0), -- pins.id: 8
    ('uid-pin-logic-G#outputBooleanObject',     9,  0), -- pins.id: 9
    ('uid-pin-logic-G#outputByteObject',        10, 0), -- pins.id: 10
    ('uid-pin-logic-G#outputCharacterObject',   11, 0), -- pins.id: 11
    ('uid-pin-logic-G#outputDoubleObject',      12, 0), -- pins.id: 12
    ('uid-pin-logic-G#outputFloatObject',       13, 0), -- pins.id: 13
    ('uid-pin-logic-G#outputIntegerObject',     14, 0), -- pins.id: 14
    ('uid-pin-logic-G#outputLongObject',        15, 0), -- pins.id: 15
    ('uid-pin-logic-G#outputShortObject',       16, 0), -- pins.id: 16
    ('uid-pin-logic-G#outputString',            17, 0); -- pins.id: 17
INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1,  '1',                       'java.lang.Boolean'  ),
    (2,  '127',                     'java.lang.Byte'     ),
    (3,  '\uFFFF',                  'java.lang.Character'),
    (4,  '1.7976931348623157e+308', 'java.lang.Double'   ),
    (5,  '3.4028235e+38f',          'java.lang.Float'    ),
    (6,  '2147483647',              'java.lang.Integer'  ),
    (7,  '9223372036854775807',     'java.lang.Long'     ),
    (8,  '32767',                   'java.lang.Short'    ),
    (9,  '0',                       'java.lang.Boolean'  ),
    (10, '-128',                    'java.lang.Byte'     ),
    (11, '\u0000',                  'java.lang.Character'),
    (12, '4.9e-324',                'java.lang.Double'   ),
    (13, '1.4e-45f',                'java.lang.Float'    ),
    (14, '-2147483648',             'java.lang.Integer'  ),
    (15, '-9223372036854775808',    'java.lang.Long'     ),
    (16, '-32768',                  'java.lang.Short'    ),
    (17, 'Lorem Ipsum',             'java.lang.String'   );
