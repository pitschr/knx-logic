--
-- COMPONENTS
--   ComponentType#INBOX (ordinal = 1)
--
INSERT INTO components (componentType, className, uid) VALUES
    (1, 'li.pitschmann.knx.logic.components.inbox.DPT2Inbox', 'uid-component-inbox-DPT2');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'knx', '3171');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'controlled'), -- connectors.id: 1
    (1, 0, 'boolValue' ); -- connectors.id: 2

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-inbox-DPT2#controlled', 0), -- pins.id: 1
    (2, 'uid-pin-inbox-DPT2#boolValue',  0); -- pins.id: 2

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
