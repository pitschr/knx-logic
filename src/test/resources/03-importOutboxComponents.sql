--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0), BindingType#DYNAMIC (ordinal = 1)
--
-- PINS
-- PINS_HISTORY (lastValue)
-- EVENT_KEYS

-- Outbox KNX DPT-1
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.DPT1Outbox', 'uid-component-outbox-A');     -- component: 1
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (1, 0, 'boolValue');                                                                       -- connector: 1
INSERT INTO pins (connectorId, uid, index)                       VALUES (1, 'uid-pin-outbox-A#boolValue',  0);                                                     -- pin: 1
INSERT INTO pins_history (pinId, value, valueType)               VALUES (1, '0', 'java.lang.Boolean');                                                             -- lastValue: boolValue = false
INSERT INTO event_keys (componentId, channel, key)               VALUES (1, 'knx', '8711');                                                                        -- event key: 1

-- Outbox KNX DPT-2
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.DPT2Outbox', 'uid-component-outbox-B');     -- component: 2
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'controlled');                                                                      -- connector: 2
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (2, 0, 'boolValue');                                                                       -- connector: 3
INSERT INTO pins (connectorId, uid, index)                       VALUES (2, 'uid-pin-outbox-B#controlled', 0);                                                     -- pin: 2
INSERT INTO pins (connectorId, uid, index)                       VALUES (3, 'uid-pin-outbox-B#boolValue',  0);                                                     -- pin: 3
INSERT INTO pins_history (pinId, value, valueType)               VALUES (2, '0', 'java.lang.Boolean');                                                             -- lastValue: controlled = false
INSERT INTO pins_history (pinId, value, valueType)               VALUES (3, '1', 'java.lang.Boolean');                                                             -- lastValue: boolValue  = true
INSERT INTO event_keys (componentId, channel, key)               VALUES (2, 'knx', '6733');                                                                        -- event key: 2

-- Outbox VARIABLE
INSERT INTO components (componentType, className, uid)           VALUES (2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox', 'uid-component-outbox-C'); -- component: 3
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (3, 0, 'data');                                                                            -- connector: 4
INSERT INTO pins (connectorId, uid, index)                       VALUES (4, 'uid-pin-outbox-C#data',       0);                                                     -- pin: 4
INSERT INTO pins_history (pinId, value, valueType)               VALUES (4, 'Hello Earth', 'java.lang.String');                                                    -- lastValue: data = "Hello Earth"
INSERT INTO event_keys (componentId, channel, key)               VALUES (3, 'var', 'barfoo');                                                                      -- event key: 3
