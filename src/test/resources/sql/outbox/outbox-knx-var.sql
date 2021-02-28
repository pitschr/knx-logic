--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
INSERT INTO components (componentType, className, uid) VALUES
    (2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox', 'uid-component-outbox-VAR');

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
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'data');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-outbox-VAR#data', 0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, 'Hello Earth', 'java.lang.String');
