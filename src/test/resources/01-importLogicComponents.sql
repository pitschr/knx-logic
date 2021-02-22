--
-- LOGIC COMPONENTS
--   ComponentType#LOGIC (ordinal = 0)
--
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicA', 'uid-component-logic-A'); -- component: 1  (no input         / no output        )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicB', 'uid-component-logic-B'); -- component: 2  (static input     / no output        )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicC', 'uid-component-logic-C'); -- component: 3  (static input     / static output    )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicE', 'uid-component-logic-E'); -- component: 4  (dynamic input    / no output        )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicF', 'uid-component-logic-F'); -- component: 5  (dynamic input    / dynamic output   )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicH', 'uid-component-logic-H'); -- component: 6  (8x static input  / 9x static output )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicI', 'uid-component-logic-I'); -- component: 7  (8x dynamic input / 1x dynamic output)
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicJ', 'uid-component-logic-J'); -- component: 8  (2x static input  / 2x static output )
INSERT INTO components (componentType, className, uid) VALUES (0, 'test.components.LogicK', 'uid-component-logic-K'); -- component: 9  (2x dynamic input / 2x static output )
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.MyFooBarLogic',        'uid-component-logic-foobar'); -- component: 10 (taken from foobar-logic.jar)
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.bitwise.CoreLogicA',   'uid-component-logic-core-A'); -- component: 11 (taken from core-logic.jar)
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.bitwise.CoreLogicB',   'uid-component-logic-core-B'); -- component: 12 (taken from core-logic.jar)
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.general.CoreLogicC',   'uid-component-logic-core-C'); -- component: 13 (taken from core-logic.jar)
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.subA.subB.CoreLogicD', 'uid-component-logic-core-D'); -- component: 14 (taken from core-logic.jar)
INSERT INTO components (componentType, className, uid) VALUES (0, 'my.logic.text.CoreLogicE',      'uid-component-logic-core-E'); -- component: 15 (taken from core-logic.jar)

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
-- Logic A
-- (empty)
-- Logic B
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'i'                      );     -- connector: 1
-- Logic C
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'i'                      );     -- connector: 2
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'o'                      );     -- connector: 3
-- Logic E
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (4, 1, 'i'                      );     -- connector: 4
-- Logic F
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (5, 1, 'i'                      );     -- connector: 5
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (5, 1, 'o'                      );     -- connector: 6
-- Logic H
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputBooleanPrimitive'  );     -- connector: 7
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputBytePrimitive'     );     -- connector: 8
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputCharacterPrimitive');     -- connector: 9
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputDoublePrimitive'   );     -- connector: 10
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputFloatPrimitive'    );     -- connector: 11
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputIntegerPrimitive'  );     -- connector: 12
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputLongPrimitive'     );     -- connector: 13
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'inputShortPrimitive'    );     -- connector: 14
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputBooleanObject'    );     -- connector: 15
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputByteObject'       );     -- connector: 16
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputCharacterObject'  );     -- connector: 17
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputDoubleObject'     );     -- connector: 18
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputFloatObject'      );     -- connector: 19
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputIntegerObject'    );     -- connector: 20
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputLongObject'       );     -- connector: 21
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputShortObject'      );     -- connector: 22
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (6, 0, 'outputString'           );     -- connector: 23
-- Logic I
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'booleans'               );     -- connector: 24
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'bytes'                  );     -- connector: 25
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'chars'                  );     -- connector: 26
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'doubles'                );     -- connector: 27
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'floats'                 );     -- connector: 28
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'integers'               );     -- connector: 29
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'longs'                  );     -- connector: 30
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'shorts'                 );     -- connector: 31
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (7, 1, 'strings'                );     -- connector: 32
-- Logic J
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (8, 0, 'inputFirst'             );     -- connector: 33
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (8, 0, 'inputSecond'            );     -- connector: 34
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (8, 0, 'outputFirst'            );     -- connector: 35
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (8, 0, 'outputSecond'           );     -- connector: 36
-- Logic K
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (9, 1, 'inputFirst'             );     -- connector: 37
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (9, 1, 'inputSecond'            );     -- connector: 38
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (9, 1, 'outputFirst'            );     -- connector: 39
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (9, 1, 'outputSecond'           );     -- connector: 40
-- Logic FooBar
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (10, 0, 'inputText'             );     -- connector: 41
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (10, 0, 'outputText'            );     -- connector: 42
-- Logic Core A
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (11, 1, 'inputs'                );     -- connector: 43
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (11, 0, 'output'                );     -- connector: 44
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (11, 0, 'outputNegation'        );     -- connector: 45
-- Logic Core B
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (12, 1, 'inputs'                );     -- connector: 46
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (12, 0, 'output'                );     -- connector: 47
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (12, 0, 'outputNegation'        );     -- connector: 48
-- Logic Core C
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (13, 1, 'inputs'                );     -- connector: 49
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (13, 0, 'output'                );     -- connector: 50
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (13, 0, 'outputNegation'        );     -- connector: 51
-- Logic Core D
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (14, 1, 'inputs'                );     -- connector: 52
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (14, 0, 'output'                );     -- connector: 53
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (14, 0, 'outputNegation'        );     -- connector: 54
-- Logic Core E
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (15, 1, 'inputs'                );     -- connector: 55
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (15, 0, 'output'                );     -- connector: 56
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (15, 0, 'outputNegation'        );     -- connector: 57

--
-- PINS
--
-- Logic A
-- (empty)
-- Logic B
INSERT INTO pins (connectorId, uid, index)         VALUES (1,  'uid-pin-logic-B#i',                       0); -- pin: 1,   i = true
INSERT INTO pins_history (pinId, value, valueType) VALUES (1, '1', 'java.lang.Boolean');
-- Logic C
INSERT INTO pins (connectorId, uid, index)         VALUES (2,  'uid-pin-logic-C#i',                       0); -- pin: 2,   i = true
INSERT INTO pins (connectorId, uid, index)         VALUES (3,  'uid-pin-logic-C#o',                       0); -- pin: 3,   o = false
INSERT INTO pins_history (pinId, value, valueType) VALUES (2, '1', 'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (3, '0', 'java.lang.Boolean');
-- Logic E
INSERT INTO pins (connectorId, uid, index)         VALUES (4,  'uid-pin-logic-E#i[0]',                    0); -- pin: 4,   i[0] = true
INSERT INTO pins (connectorId, uid, index)         VALUES (4,  'uid-pin-logic-E#i[1]',                    1); -- pin: 5,   i[1] = false
INSERT INTO pins_history (pinId, value, valueType) VALUES (4, '1', 'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (5, '0', 'java.lang.Boolean');

-- Logic F
INSERT INTO pins (connectorId, uid, index)         VALUES (5,  'uid-pin-logic-F#i[0]',                    0); -- pin: 6,   i[0] = true
INSERT INTO pins (connectorId, uid, index)         VALUES (5,  'uid-pin-logic-F#i[1]',                    1); -- pin: 7,   i[1] = false
INSERT INTO pins (connectorId, uid, index)         VALUES (6,  'uid-pin-logic-F#o[0]',                    0); -- pin: 8,   o[0] = false
INSERT INTO pins (connectorId, uid, index)         VALUES (6,  'uid-pin-logic-F#o[1]',                    1); -- pin: 9,   o[1] = true
INSERT INTO pins_history (pinId, value, valueType) VALUES (6, '1', 'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (7, '0', 'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (8, '0', 'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (9, '1', 'java.lang.Boolean');
-- Logic H
INSERT INTO pins (connectorId, uid, index)         VALUES (7,  'uid-pin-logic-I#inputBooleanPrimitive',   0); -- pin: 10
INSERT INTO pins (connectorId, uid, index)         VALUES (8,  'uid-pin-logic-I#inputBytePrimitive',      0); -- pin: 11
INSERT INTO pins (connectorId, uid, index)         VALUES (9,  'uid-pin-logic-I#inputCharacterPrimitive', 0); -- pin: 12
INSERT INTO pins (connectorId, uid, index)         VALUES (10, 'uid-pin-logic-I#inputDoublePrimitive',    0); -- pin: 13
INSERT INTO pins (connectorId, uid, index)         VALUES (11, 'uid-pin-logic-I#inputFloatPrimitive',     0); -- pin: 14
INSERT INTO pins (connectorId, uid, index)         VALUES (12, 'uid-pin-logic-I#inputIntegerPrimitive',   0); -- pin: 15
INSERT INTO pins (connectorId, uid, index)         VALUES (13, 'uid-pin-logic-I#inputLongPrimitive',      0); -- pin: 16
INSERT INTO pins (connectorId, uid, index)         VALUES (14, 'uid-pin-logic-I#inputShortPrimitive',     0); -- pin: 17
INSERT INTO pins (connectorId, uid, index)         VALUES (15, 'uid-pin-logic-I#outputBooleanObject',     0); -- pin: 18
INSERT INTO pins (connectorId, uid, index)         VALUES (16, 'uid-pin-logic-I#outputByteObject',        0); -- pin: 19
INSERT INTO pins (connectorId, uid, index)         VALUES (17, 'uid-pin-logic-I#outputCharacterObject',   0); -- pin: 20
INSERT INTO pins (connectorId, uid, index)         VALUES (18, 'uid-pin-logic-I#outputDoubleObject',      0); -- pin: 21
INSERT INTO pins (connectorId, uid, index)         VALUES (19, 'uid-pin-logic-I#outputFloatObject',       0); -- pin: 22
INSERT INTO pins (connectorId, uid, index)         VALUES (20, 'uid-pin-logic-I#outputIntegerObject',     0); -- pin: 23
INSERT INTO pins (connectorId, uid, index)         VALUES (21, 'uid-pin-logic-I#outputLongObject',        0); -- pin: 24
INSERT INTO pins (connectorId, uid, index)         VALUES (22, 'uid-pin-logic-I#outputShortObject',       0); -- pin: 25
INSERT INTO pins (connectorId, uid, index)         VALUES (23, 'uid-pin-logic-I#outputString',            0); -- pin: 26
INSERT INTO pins_history (pinId, value, valueType) VALUES (10, '1',                       'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (11, '127',                     'java.lang.Byte');
INSERT INTO pins_history (pinId, value, valueType) VALUES (12, '\uFFFF',                  'java.lang.Character');
INSERT INTO pins_history (pinId, value, valueType) VALUES (13, '1.7976931348623157e+308', 'java.lang.Double');
INSERT INTO pins_history (pinId, value, valueType) VALUES (14, '3.4028235e+38f',          'java.lang.Float');
INSERT INTO pins_history (pinId, value, valueType) VALUES (15, '2147483647',              'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (16, '9223372036854775807',     'java.lang.Long');
INSERT INTO pins_history (pinId, value, valueType) VALUES (17, '32767',                   'java.lang.Short');
INSERT INTO pins_history (pinId, value, valueType) VALUES (18, '0',                       'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (19, '-128',                    'java.lang.Byte');
INSERT INTO pins_history (pinId, value, valueType) VALUES (20, '\u0000',                  'java.lang.Character');
INSERT INTO pins_history (pinId, value, valueType) VALUES (21, '4.9e-324',                'java.lang.Double');
INSERT INTO pins_history (pinId, value, valueType) VALUES (22, '1.4e-45f',                'java.lang.Float');
INSERT INTO pins_history (pinId, value, valueType) VALUES (23, '-2147483648',             'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (24, '-9223372036854775808',    'java.lang.Long');
INSERT INTO pins_history (pinId, value, valueType) VALUES (25, '-32768',                  'java.lang.Short');
INSERT INTO pins_history (pinId, value, valueType) VALUES (26, 'Lorem Ipsum',             'java.lang.String');

-- Logic I
INSERT INTO pins (connectorId, uid, index)         VALUES (24, 'uid-pin-logic-I#booleans[0]', 0); -- pin: 27
INSERT INTO pins (connectorId, uid, index)         VALUES (24, 'uid-pin-logic-I#booleans[1]', 1); -- pin: 28
INSERT INTO pins (connectorId, uid, index)         VALUES (25, 'uid-pin-logic-I#bytes[0]',    0); -- pin: 29
INSERT INTO pins (connectorId, uid, index)         VALUES (25, 'uid-pin-logic-I#bytes[1]',    1); -- pin: 30
INSERT INTO pins (connectorId, uid, index)         VALUES (26, 'uid-pin-logic-I#chars[0]',    0); -- pin: 31
INSERT INTO pins (connectorId, uid, index)         VALUES (26, 'uid-pin-logic-I#chars[1]',    1); -- pin: 32
INSERT INTO pins (connectorId, uid, index)         VALUES (27, 'uid-pin-logic-I#doubles[0]',  0); -- pin: 33
INSERT INTO pins (connectorId, uid, index)         VALUES (27, 'uid-pin-logic-I#doubles[1]',  1); -- pin: 34
INSERT INTO pins (connectorId, uid, index)         VALUES (28, 'uid-pin-logic-I#floats[0]',   0); -- pin: 35
INSERT INTO pins (connectorId, uid, index)         VALUES (28, 'uid-pin-logic-I#floats[1]',   1); -- pin: 36
INSERT INTO pins (connectorId, uid, index)         VALUES (29, 'uid-pin-logic-I#integers[0]', 0); -- pin: 37
INSERT INTO pins (connectorId, uid, index)         VALUES (29, 'uid-pin-logic-I#integers[1]', 1); -- pin: 38
INSERT INTO pins (connectorId, uid, index)         VALUES (30, 'uid-pin-logic-I#longs[0]',    0); -- pin: 39
INSERT INTO pins (connectorId, uid, index)         VALUES (30, 'uid-pin-logic-I#longs[1]',    1); -- pin: 40
INSERT INTO pins (connectorId, uid, index)         VALUES (31, 'uid-pin-logic-I#shorts[0]',   0); -- pin: 41
INSERT INTO pins (connectorId, uid, index)         VALUES (31, 'uid-pin-logic-I#shorts[1]',   1); -- pin: 42
INSERT INTO pins (connectorId, uid, index)         VALUES (32, 'uid-pin-logic-I#strings[0]',  0); -- pin: 43
INSERT INTO pins (connectorId, uid, index)         VALUES (32, 'uid-pin-logic-I#strings[1]',  1); -- pin: 44
INSERT INTO pins_history (pinId, value, valueType) VALUES (27, '0',                       'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (28, '1',                       'java.lang.Boolean');
INSERT INTO pins_history (pinId, value, valueType) VALUES (29, '-128',                    'java.lang.Byte');
INSERT INTO pins_history (pinId, value, valueType) VALUES (30, '127',                     'java.lang.Byte');
INSERT INTO pins_history (pinId, value, valueType) VALUES (31, '\u0000',                  'java.lang.Character');
INSERT INTO pins_history (pinId, value, valueType) VALUES (32, '\uFFFF',                  'java.lang.Character');
INSERT INTO pins_history (pinId, value, valueType) VALUES (33, '4.9e-324',                'java.lang.Double');
INSERT INTO pins_history (pinId, value, valueType) VALUES (34, '1.7976931348623157e+308', 'java.lang.Double');
INSERT INTO pins_history (pinId, value, valueType) VALUES (35, '1.4e-45f',                'java.lang.Float');
INSERT INTO pins_history (pinId, value, valueType) VALUES (36, '3.4028235e+38f',          'java.lang.Float');
INSERT INTO pins_history (pinId, value, valueType) VALUES (37, '-2147483648',             'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (38, '2147483647',              'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (39, '-9223372036854775808',    'java.lang.Long');
INSERT INTO pins_history (pinId, value, valueType) VALUES (40, '9223372036854775807',     'java.lang.Long');
INSERT INTO pins_history (pinId, value, valueType) VALUES (41, '-32768',                  'java.lang.Short');
INSERT INTO pins_history (pinId, value, valueType) VALUES (42, '32767',                   'java.lang.Short');
INSERT INTO pins_history (pinId, value, valueType) VALUES (43, 'Lorem Ipsum',             'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (44, 'Dolor Sit Amet',          'java.lang.String');
-- Logic J
INSERT INTO pins (connectorId, uid, index)         VALUES (33, 'uid-pin-logic-J#inputFirst',   0); -- pin: 45
INSERT INTO pins (connectorId, uid, index)         VALUES (34, 'uid-pin-logic-J#inputSecond',  0); -- pin: 46
INSERT INTO pins (connectorId, uid, index)         VALUES (35, 'uid-pin-logic-J#outputFirst',  0); -- pin: 47
INSERT INTO pins (connectorId, uid, index)         VALUES (36, 'uid-pin-logic-J#outputSecond', 0); -- pin: 48
INSERT INTO pins_history (pinId, value, valueType) VALUES (45, '4711',  'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (46, '13',    'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (47, 'Hello', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (48, 'World', 'java.lang.String');
-- Logic K
INSERT INTO pins (connectorId, uid, index)         VALUES (37, 'uid-pin-logic-K#inputFirst[0]',   0); -- pin: 49
INSERT INTO pins (connectorId, uid, index)         VALUES (37, 'uid-pin-logic-K#inputFirst[1]',   1); -- pin: 50
INSERT INTO pins (connectorId, uid, index)         VALUES (37, 'uid-pin-logic-K#inputFirst[2]',   2); -- pin: 51
INSERT INTO pins (connectorId, uid, index)         VALUES (38, 'uid-pin-logic-K#inputSecond[0]',  0); -- pin: 52
INSERT INTO pins (connectorId, uid, index)         VALUES (38, 'uid-pin-logic-K#inputSecond[1]',  1); -- pin: 53
INSERT INTO pins (connectorId, uid, index)         VALUES (39, 'uid-pin-logic-K#outputFirst[0]',  0); -- pin: 54
INSERT INTO pins (connectorId, uid, index)         VALUES (39, 'uid-pin-logic-K#outputFirst[1]',  1); -- pin: 55
INSERT INTO pins (connectorId, uid, index)         VALUES (39, 'uid-pin-logic-K#outputFirst[2]',  2); -- pin: 56
INSERT INTO pins (connectorId, uid, index)         VALUES (39, 'uid-pin-logic-K#outputFirst[3]',  3); -- pin: 57
INSERT INTO pins (connectorId, uid, index)         VALUES (40, 'uid-pin-logic-K#outputSecond[0]', 0); -- pin: 58
INSERT INTO pins (connectorId, uid, index)         VALUES (40, 'uid-pin-logic-K#outputSecond[1]', 1); -- pin: 59
INSERT INTO pins_history (pinId, value, valueType) VALUES (49, '1024',  'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (50, '4201',  'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (51, '2014',  'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (52, '17',    'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (53, '71',    'java.lang.Integer');
INSERT INTO pins_history (pinId, value, valueType) VALUES (54, 'Hello', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (55, 'olleH', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (56, 'lloeH', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (57, 'Hoell', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (58, 'World', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (59, 'dlroW', 'java.lang.String');
-- Logic FooBar
INSERT INTO pins (connectorId, uid, index)         VALUES (41, 'uid-pin-logic-foobar#inputText',  0); -- pin: 60
INSERT INTO pins (connectorId, uid, index)         VALUES (42, 'uid-pin-logic-foobar#outputText', 0); -- pin: 61
INSERT INTO pins_history (pinId, value, valueType) VALUES (60, 'foo', 'java.lang.String');
INSERT INTO pins_history (pinId, value, valueType) VALUES (61, 'bar', 'java.lang.String');
-- Logic Core A
INSERT INTO pins (connectorId, uid, index)         VALUES (43, 'uid-pin-logic-core-A#inputs[0]',      0); -- pin: 62
INSERT INTO pins (connectorId, uid, index)         VALUES (43, 'uid-pin-logic-core-A#inputs[1]',      1); -- pin: 63
INSERT INTO pins (connectorId, uid, index)         VALUES (44, 'uid-pin-logic-core-A#output',         0); -- pin: 64
INSERT INTO pins (connectorId, uid, index)         VALUES (45, 'uid-pin-logic-core-A#outputNegation', 0); -- pin: 65
-- Logic Core B
INSERT INTO pins (connectorId, uid, index)         VALUES (46, 'uid-pin-logic-core-B#inputs[0]',      0); -- pin: 66
INSERT INTO pins (connectorId, uid, index)         VALUES (46, 'uid-pin-logic-core-B#inputs[1]',      1); -- pin: 67
INSERT INTO pins (connectorId, uid, index)         VALUES (46, 'uid-pin-logic-core-B#inputs[2]',      2); -- pin: 68
INSERT INTO pins (connectorId, uid, index)         VALUES (47, 'uid-pin-logic-core-B#output',         0); -- pin: 69
INSERT INTO pins (connectorId, uid, index)         VALUES (48, 'uid-pin-logic-core-B#outputNegation', 0); -- pin: 70
-- Logic Core C
INSERT INTO pins (connectorId, uid, index)         VALUES (49, 'uid-pin-logic-core-C#inputs[0]',      0); -- pin: 71
INSERT INTO pins (connectorId, uid, index)         VALUES (49, 'uid-pin-logic-core-C#inputs[1]',      1); -- pin: 72
INSERT INTO pins (connectorId, uid, index)         VALUES (49, 'uid-pin-logic-core-C#inputs[2]',      2); -- pin: 73
INSERT INTO pins (connectorId, uid, index)         VALUES (49, 'uid-pin-logic-core-C#inputs[3]',      3); -- pin: 74
INSERT INTO pins (connectorId, uid, index)         VALUES (50, 'uid-pin-logic-core-C#output',         0); -- pin: 75
INSERT INTO pins (connectorId, uid, index)         VALUES (51, 'uid-pin-logic-core-C#outputNegation', 0); -- pin: 76
-- Logic Core D
INSERT INTO pins (connectorId, uid, index)         VALUES (52, 'uid-pin-logic-core-D#inputs[0]',      0); -- pin: 77
INSERT INTO pins (connectorId, uid, index)         VALUES (52, 'uid-pin-logic-core-D#inputs[1]',      1); -- pin: 78
INSERT INTO pins (connectorId, uid, index)         VALUES (52, 'uid-pin-logic-core-D#inputs[2]',      2); -- pin: 79
INSERT INTO pins (connectorId, uid, index)         VALUES (52, 'uid-pin-logic-core-D#inputs[3]',      3); -- pin: 80
INSERT INTO pins (connectorId, uid, index)         VALUES (52, 'uid-pin-logic-core-D#inputs[4]',      4); -- pin: 81
INSERT INTO pins (connectorId, uid, index)         VALUES (53, 'uid-pin-logic-core-D#output',         0); -- pin: 82
INSERT INTO pins (connectorId, uid, index)         VALUES (54, 'uid-pin-logic-core-D#outputNegation', 0); -- pin: 83
-- Logic Core E
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[0]',      0); -- pin: 84
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[1]',      1); -- pin: 85
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[2]',      2); -- pin: 86
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[3]',      3); -- pin: 87
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[4]',      4); -- pin: 88
INSERT INTO pins (connectorId, uid, index)         VALUES (55, 'uid-pin-logic-core-E#inputs[5]',      5); -- pin: 89
INSERT INTO pins (connectorId, uid, index)         VALUES (56, 'uid-pin-logic-core-E#output',         0); -- pin: 90
INSERT INTO pins (connectorId, uid, index)         VALUES (57, 'uid-pin-logic-core-E#outputNegation', 0); -- pin: 91
