--
-- COMPONENTS
--   ComponentType#INBOX (ordinal = 1)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-inbox-VAR', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'var', 'foobar');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-inbox-VAR#data', 1, 0, 'data');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-inbox-VAR#data', 1, 0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'Hello World', 'java.lang.String');
