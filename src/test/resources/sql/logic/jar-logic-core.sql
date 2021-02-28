--
-- LOGIC COMPONENTS
--   ComponentType#LOGIC (ordinal = 0)
--
INSERT INTO components (componentType, className, uid) VALUES
    (0, 'my.logic.bitwise.CoreLogicA',   'uid-component-logic-core-A'), -- components.id: 1 (taken from core-logic.jar)
    (0, 'my.logic.bitwise.CoreLogicB',   'uid-component-logic-core-B'), -- components.id: 2 (taken from core-logic.jar)
    (0, 'my.logic.general.CoreLogicC',   'uid-component-logic-core-C'), -- components.id: 3 (taken from core-logic.jar)
    (0, 'my.logic.subA.subB.CoreLogicD', 'uid-component-logic-core-D'), -- components.id: 4 (taken from core-logic.jar)
    (0, 'my.logic.text.CoreLogicE',      'uid-component-logic-core-E'); -- components.id: 5 (taken from core-logic.jar)

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
-- Logic Core A
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 1, 'inputs'        ), -- connectors.id: 1
    (1, 0, 'output'        ), -- connectors.id: 2
    (1, 0, 'outputNegation'); -- connectors.id: 3
-- Logic Core B
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (2, 1, 'inputs'        ), -- connectors.id: 4
    (2, 0, 'output'        ), -- connectors.id: 5
    (2, 0, 'outputNegation'); -- connectors.id: 6
-- Logic Core C
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (3, 1, 'inputs'        ), -- connectors.id: 7
    (3, 0, 'output'        ), -- connectors.id: 8
    (3, 0, 'outputNegation'), -- connectors.id: 9
-- Logic Core D
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (4, 1, 'inputs'        ), -- connectors.id: 10
    (4, 0, 'output'        ), -- connectors.id: 11
    (4, 0, 'outputNegation'); -- connectors.id: 12
-- Logic Core E
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (5, 1, 'inputs'        ), -- connectors.id: 13
    (5, 0, 'output'        ), -- connectors.id: 14
    (5, 0, 'outputNegation'); -- connectors.id: 15

--
-- PINS
--
-- Logic Core A
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-logic-core-A#inputs[0]',      0), -- pins.id: 1
    (1, 'uid-pin-logic-core-A#inputs[1]',      1), -- pins.id: 2
    (2, 'uid-pin-logic-core-A#output',         0), -- pins.id: 3
    (3, 'uid-pin-logic-core-A#outputNegation', 0); -- pins.id: 4
-- Logic Core B
INSERT INTO pins (connectorId, uid, index) VALUES
    (4, 'uid-pin-logic-core-B#inputs[0]',      0), -- pins.id: 5
    (4, 'uid-pin-logic-core-B#inputs[1]',      1), -- pins.id: 6
    (4, 'uid-pin-logic-core-B#inputs[2]',      2), -- pins.id: 7
    (5, 'uid-pin-logic-core-B#output',         0), -- pins.id: 8
    (6, 'uid-pin-logic-core-B#outputNegation', 0); -- pins.id: 9
-- Logic Core C
INSERT INTO pins (connectorId, uid, index) VALUES
    (7, 'uid-pin-logic-core-C#inputs[0]',      0), -- pins.id: 10
    (7, 'uid-pin-logic-core-C#inputs[1]',      1), -- pins.id: 11
    (7, 'uid-pin-logic-core-C#inputs[2]',      2), -- pins.id: 12
    (7, 'uid-pin-logic-core-C#inputs[3]',      3), -- pins.id: 13
    (8, 'uid-pin-logic-core-C#output',         0), -- pins.id: 14
    (9, 'uid-pin-logic-core-C#outputNegation', 0); -- pins.id: 15
-- Logic Core D
INSERT INTO pins (connectorId, uid, index) VALUES
    (10, 'uid-pin-logic-core-D#inputs[0]',      0), -- pins.id: 16
    (10, 'uid-pin-logic-core-D#inputs[1]',      1), -- pins.id: 17
    (10, 'uid-pin-logic-core-D#inputs[2]',      2), -- pins.id: 18
    (10, 'uid-pin-logic-core-D#inputs[3]',      3), -- pins.id: 19
    (10, 'uid-pin-logic-core-D#inputs[4]',      4), -- pins.id: 20
    (11, 'uid-pin-logic-core-D#output',         0), -- pins.id: 21
    (12, 'uid-pin-logic-core-D#outputNegation', 0); -- pins.id: 22
-- Logic Core E
INSERT INTO pins (connectorId, uid, index) VALUES
    (13, 'uid-pin-logic-core-E#inputs[0]',      0), -- pins.id: 23
    (13, 'uid-pin-logic-core-E#inputs[1]',      1), -- pins.id: 24
    (13, 'uid-pin-logic-core-E#inputs[2]',      2), -- pins.id: 25
    (13, 'uid-pin-logic-core-E#inputs[3]',      3), -- pins.id: 26
    (13, 'uid-pin-logic-core-E#inputs[4]',      4), -- pins.id: 27
    (13, 'uid-pin-logic-core-E#inputs[5]',      5), -- pins.id: 28
    (14, 'uid-pin-logic-core-E#output',         0), -- pins.id: 29
    (15, 'uid-pin-logic-core-E#outputNegation', 0); -- pins.id: 30
