--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-outbox-VAR', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');

--
-- EVENT KEYS
--
INSERT INTO event_keys (componentId, channel, key) VALUES
    (1, 'var', 'barfoo');

--
-- CONNECTORS
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-outbox-VAR#data', 1, 0, 'data');

--
-- PINS
--
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-outbox-VAR#data', 1,0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'Hello Earth', 'java.lang.String');
