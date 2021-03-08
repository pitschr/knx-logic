--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-outbox-DPT2', 2, 'li.pitschmann.knx.logic.components.outbox.DPT2Outbox');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'knx', '6733');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-outbox-DPT2#controlled', 1, 0, 'controlled'), -- connectors.id: 1
    ('uid-connector-outbox-DPT2#boolValue',  1, 0, 'boolValue' ); -- connectors.id: 2

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-outbox-DPT2#controlled', 1, 0), -- pins.id: 1
    ('uid-pin-outbox-DPT2#boolValue',  2, 0); -- pins.id: 2

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '0', 'java.lang.Boolean'),
    (2, '1', 'java.lang.Boolean');
