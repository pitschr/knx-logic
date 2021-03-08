--
-- COMPONENTS
--   ComponentType#INBOX (ordinal = 1)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-inbox-DPT2', 1, 'li.pitschmann.knx.logic.components.inbox.DPT2Inbox');

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
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-inbox-DPT2#controlled', 1, 0, 'controlled'), -- connectors.id: 1
    ('uid-connector-inbox-DPT2#boolValue',  1, 0, 'boolValue' ); -- connectors.id: 2

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-inbox-DPT2#controlled', 1, 0), -- pins.id: 1
    ('uid-pin-inbox-DPT2#boolValue',  2, 0); -- pins.id: 2

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean'),
    (2, '0', 'java.lang.Boolean');
