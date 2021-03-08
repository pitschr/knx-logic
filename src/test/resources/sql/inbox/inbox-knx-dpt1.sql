--
-- COMPONENTS
--   ComponentType#INBOX (ordinal = 1)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-inbox-DPT1', 1, 'li.pitschmann.knx.logic.components.inbox.DPT1Inbox');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'knx', '4711');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-inbox-DPT1#boolValue', 1, 0, 'boolValue');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-inbox-DPT1#boolValue', 1, 0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '1', 'java.lang.Boolean');
