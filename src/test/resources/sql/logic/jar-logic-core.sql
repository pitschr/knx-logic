--
-- LOGIC COMPONENTS
--   ComponentType#LOGIC (ordinal = 0)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic-core-A', 0, 'my.logic.bitwise.CoreLogicA', ), -- components.id: 1 (taken from core-logic.jar)
    ('uid-component-logic-core-B', 0, 'my.logic.bitwise.CoreLogicB', ), -- components.id: 2 (taken from core-logic.jar)
    ('uid-component-logic-core-C', 0, 'my.logic.general.CoreLogicC', ), -- components.id: 3 (taken from core-logic.jar)
    ('uid-component-logic-core-D', 0, 'my.logic.subA.subB.CoreLogicD'), -- components.id: 4 (taken from core-logic.jar)
    ('uid-component-logic-core-E', 0, 'my.logic.text.CoreLogicE',    ); -- components.id: 5 (taken from core-logic.jar)

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
-- Logic Core A
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-core-A#inputs',         1, 1, 'inputs'        ), -- connectors.id: 1
    ('uid-connector-logic-core-A#output',         1, 0, 'output'        ), -- connectors.id: 2
    ('uid-connector-logic-core-A#outputNegation', 1, 0, 'outputNegation'); -- connectors.id: 3
-- Logic Core B
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-core-B#inputs',         2, 1, 'inputs'        ), -- connectors.id: 4
    ('uid-connector-logic-core-B#output',         2, 0, 'output'        ), -- connectors.id: 5
    ('uid-connector-logic-core-B#outputNegation', 2, 0, 'outputNegation'); -- connectors.id: 6
-- Logic Core C
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-core-C#inputs',         3, 1, 'inputs'        ), -- connectors.id: 7
    ('uid-connector-logic-core-C#output',         3, 0, 'output'        ), -- connectors.id: 8
    ('uid-connector-logic-core-C#outputNegation', 3, 0, 'outputNegation'), -- connectors.id: 9
-- Logic Core D
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-core-D#inputs',         4, 1, 'inputs'        ), -- connectors.id: 10
    ('uid-connector-logic-core-D#output',         4, 0, 'output'        ), -- connectors.id: 11
    ('uid-connector-logic-core-D#outputNegation', 4, 0, 'outputNegation'); -- connectors.id: 12
-- Logic Core E
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-core-E#inputs',         5, 1, 'inputs'        ), -- connectors.id: 13
    ('uid-connector-logic-core-E#output',         5, 0, 'output'        ), -- connectors.id: 14
    ('uid-connector-logic-core-E#outputNegation', 5, 0, 'outputNegation'); -- connectors.id: 15

--
-- PINS
--
-- Logic Core A
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-core-A#inputs[0]',      1, 0), -- pins.id: 1
    ('uid-pin-logic-core-A#inputs[1]',      1, 1), -- pins.id: 2
    ('uid-pin-logic-core-A#output',         2, 0), -- pins.id: 3
    ('uid-pin-logic-core-A#outputNegation', 3, 0); -- pins.id: 4
-- Logic Core B
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-core-B#inputs[0]',      4, 0), -- pins.id: 5
    ('uid-pin-logic-core-B#inputs[1]',      4, 1), -- pins.id: 6
    ('uid-pin-logic-core-B#inputs[2]',      4, 2), -- pins.id: 7
    ('uid-pin-logic-core-B#output',         5, 0), -- pins.id: 8
    ('uid-pin-logic-core-B#outputNegation', 6, 0); -- pins.id: 9
-- Logic Core C
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-core-C#inputs[0]',      7, 0), -- pins.id: 10
    ('uid-pin-logic-core-C#inputs[1]',      7, 1), -- pins.id: 11
    ('uid-pin-logic-core-C#inputs[2]',      7, 2), -- pins.id: 12
    ('uid-pin-logic-core-C#inputs[3]',      7, 3), -- pins.id: 13
    ('uid-pin-logic-core-C#output',         8, 0), -- pins.id: 14
    ('uid-pin-logic-core-C#outputNegation', 9, 0); -- pins.id: 15
-- Logic Core D
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-core-D#inputs[0]',      10, 0), -- pins.id: 16
    ('uid-pin-logic-core-D#inputs[1]',      10, 1), -- pins.id: 17
    ('uid-pin-logic-core-D#inputs[2]',      10, 2), -- pins.id: 18
    ('uid-pin-logic-core-D#inputs[3]',      10, 3), -- pins.id: 19
    ('uid-pin-logic-core-D#inputs[4]',      10, 4), -- pins.id: 20
    ('uid-pin-logic-core-D#output',         11, 0), -- pins.id: 21
    ('uid-pin-logic-core-D#outputNegation', 12, 0); -- pins.id: 22
-- Logic Core E
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-core-E#inputs[0]',      13, 0), -- pins.id: 23
    ('uid-pin-logic-core-E#inputs[1]',      13, 1), -- pins.id: 24
    ('uid-pin-logic-core-E#inputs[2]',      13, 2), -- pins.id: 25
    ('uid-pin-logic-core-E#inputs[3]',      13, 3), -- pins.id: 26
    ('uid-pin-logic-core-E#inputs[4]',      13, 4), -- pins.id: 27
    ('uid-pin-logic-core-E#inputs[5]',      13, 5), -- pins.id: 28
    ('uid-pin-logic-core-E#output',         14, 0), -- pins.id: 29
    ('uid-pin-logic-core-E#outputNegation', 15, 0); -- pins.id: 30
