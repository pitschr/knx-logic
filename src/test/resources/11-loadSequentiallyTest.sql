--
-- LOAD SEQUENTIALLY COMPONENTS
-- (Use LogicC for this case)
--
INSERT INTO components (componentType, className, uid)           VALUES (0, 'test.components.LogicC', 'uid-component-logic-C'); -- component: 1
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'i');                                            -- connector: 1
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'o');                                            -- connector: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (1,  'uid-pin-logic-C#i', 0);                           -- pin: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (2,  'uid-pin-logic-C#o', 0);                           -- pin: 2
