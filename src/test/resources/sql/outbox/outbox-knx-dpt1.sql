--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-outbox-DPT1', 2, 'li.pitschmann.knx.logic.components.outbox.DPT1Outbox');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'knx', '8711');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-outbox-DPT1#boolValue', 1, 0, 'boolValue');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-outbox-DPT1#boolValue', 1, 0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '0', 'java.lang.Boolean');
