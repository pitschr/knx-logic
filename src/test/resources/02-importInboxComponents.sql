--
-- COMPONENTS
--   ComponentType#INBOX (ordinal = 1)
--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0), BindingType#DYNAMIC (ordinal = 1)
--
-- PINS
-- PINS_HISTORY (lastValue)
-- EVENT_KEYS
--

-- Inbox KNX DPT-1
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.DPT1Inbox', 'uid-component-inbox-A');     -- component: 1
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'boolValue');                                                                    -- connector: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-pin-inbox-A#boolValue',  0);                                                   -- pin: 1
INSERT INTO pins_history (pinId, value, valueType)               VALUES (1, '1', 'java.lang.Boolean');                                                          -- lastValue: boolValue = true
INSERT INTO event_keys (componentId, channel, key)               VALUES (1, 'knx', '4711');                                                                     -- event key: 1

-- Inbox KNX DPT-2
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.DPT2Inbox', 'uid-component-inbox-B');     -- component: 2
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'controlled');                                                                   -- connector: 2
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'boolValue' );                                                                   -- connector: 3
INSERT INTO pins (connectorId, uid, index)                       VALUES (2, 'uid-pin-inbox-B#controlled', 0);                                                   -- pin: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (3, 'uid-pin-inbox-B#boolValue',  0);                                                   -- pin: 3
INSERT INTO pins_history (pinId, value, valueType)               VALUES (2, '1', 'java.lang.Boolean');                                                          -- lastValue: controlled = true
INSERT INTO pins_history (pinId, value, valueType)               VALUES (3, '0', 'java.lang.Boolean');                                                          -- lastValue: boolValue  = false
INSERT INTO event_keys (componentId, channel, key)               VALUES (2, 'knx', '3171');                                                                     -- event key: 2

-- Inbox VARIABLE
INSERT INTO components (componentType, className, uid)           VALUES (1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox', 'uid-component-inbox-C'); -- component: 3
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'data');                                                                         -- connector: 4
INSERT INTO pins (connectorId, uid, index)                       VALUES (4, 'uid-pin-inbox-C#data',       0);                                                   -- pin: 4
INSERT INTO pins_history (pinId, value, valueType)               VALUES (4, 'Hello World', 'java.lang.String');                                                 -- lastValue: data = "Hello World"
INSERT INTO event_keys (componentId, channel, key)               VALUES (3, 'var', 'foobar');                                                                   -- event key: 3
