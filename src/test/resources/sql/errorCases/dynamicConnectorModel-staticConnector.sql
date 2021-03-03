--
-- Test with wrong SQL data:
--
-- Model Connector is dynamic
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.DPT1Inbox', 'uid-component-static'); -- component: 1
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 1, 'barName');                                                                 -- connector: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-static-1', 0);                                                            -- pin: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-static-2', 1);                                                            -- pin: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-static-3', 2);                                                            -- pin: 3
