--
-- COMPONENT
--   ComponentType#LOGIC (ordinal = 0)
--   Inputs: 8x Static
--   Outputs: 9x Static
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.LogicG', 'uid-component-logic-G');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'inputBooleanPrimitive'  ), -- connectors.id: 1
    (1, 0, 'inputBytePrimitive'     ), -- connectors.id: 2
    (1, 0, 'inputCharacterPrimitive'), -- connectors.id: 3
    (1, 0, 'inputDoublePrimitive'   ), -- connectors.id: 4
    (1, 0, 'inputFloatPrimitive'    ), -- connectors.id: 5
    (1, 0, 'inputIntegerPrimitive'  ), -- connectors.id: 6
    (1, 0, 'inputLongPrimitive'     ), -- connectors.id: 7
    (1, 0, 'inputShortPrimitive'    ), -- connectors.id: 8
    (1, 0, 'outputBooleanObject'    ), -- connectors.id: 9
    (1, 0, 'outputByteObject'       ), -- connectors.id: 10
    (1, 0, 'outputCharacterObject'  ), -- connectors.id: 11
    (1, 0, 'outputDoubleObject'     ), -- connectors.id: 12
    (1, 0, 'outputFloatObject'      ), -- connectors.id: 13
    (1, 0, 'outputIntegerObject'    ), -- connectors.id: 14
    (1, 0, 'outputLongObject'       ), -- connectors.id: 15
    (1, 0, 'outputShortObject'      ), -- connectors.id: 16
    (1, 0, 'outputString'           ); -- connectors.id: 17

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1,  'uid-pin-logic-G#inputBooleanPrimitive',   0), -- pins.id: 1
    (2,  'uid-pin-logic-G#inputBytePrimitive',      0), -- pins.id: 2
    (3,  'uid-pin-logic-G#inputCharacterPrimitive', 0), -- pins.id: 3
    (4,  'uid-pin-logic-G#inputDoublePrimitive',    0), -- pins.id: 4
    (5,  'uid-pin-logic-G#inputFloatPrimitive',     0), -- pins.id: 5
    (6,  'uid-pin-logic-G#inputIntegerPrimitive',   0), -- pins.id: 6
    (7,  'uid-pin-logic-G#inputLongPrimitive',      0), -- pins.id: 7
    (8,  'uid-pin-logic-G#inputShortPrimitive',     0), -- pins.id: 8
    (9,  'uid-pin-logic-G#outputBooleanObject',     0), -- pins.id: 9
    (10, 'uid-pin-logic-G#outputByteObject',        0), -- pins.id: 10
    (11, 'uid-pin-logic-G#outputCharacterObject',   0), -- pins.id: 11
    (12, 'uid-pin-logic-G#outputDoubleObject',      0), -- pins.id: 12
    (13, 'uid-pin-logic-G#outputFloatObject',       0), -- pins.id: 13
    (14, 'uid-pin-logic-G#outputIntegerObject',     0), -- pins.id: 14
    (15, 'uid-pin-logic-G#outputLongObject',        0), -- pins.id: 15
    (16, 'uid-pin-logic-G#outputShortObject',       0), -- pins.id: 16
    (17, 'uid-pin-logic-G#outputString',            0); -- pins.id: 17
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
