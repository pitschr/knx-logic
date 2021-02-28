--
-- COMPONENTS
--   ComponentType#OUTBOX (ordinal = 2)
--
INSERT INTO components (componentType, className, uid) VALUES
    (2, 'li.pitschmann.knx.logic.components.outbox.DPT1Outbox', 'uid-component-outbox-DPT1');

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
INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'boolValue');

--
-- PINS
--
INSERT INTO pins (connectorId, uid, index) VALUES
    (1, 'uid-pin-outbox-DPT1#boolValue',  0);

INSERT INTO pin_values (pinId, value, valueType) VALUES
    (1, '0', 'java.lang.Boolean');
