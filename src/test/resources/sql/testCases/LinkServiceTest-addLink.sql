--
--                     .----------.
--                     |   OUT 1  |
--  .---------.        `----------´
--  |   IN 1  |        .----------.
--  `---------´        |   OUT 2  |
--                     `----------´
--  .---------.        .----------.
--  |   IN 2  |        |   OUT 3  |
--  `---------´        `----------´
--
-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: Inbox (id=1)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-inbox-1', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-inbox-1', 1, 0, 'data');   -- connectors.id: 1
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-inbox-1', 1, 0);                  -- pins.id: 1
INSERT INTO event_keys (componentId, channel, key)                    VALUES (1, 'var', 'IN 1');

-- Component: Inbox (id=2)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-inbox-2', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-inbox-2', 2, 0, 'data');   -- connectors.id: 2
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-inbox-2', 2, 0);                 -- pins.id: 2
INSERT INTO event_keys (componentId, channel, key)                    VALUES (2, 'var', 'IN 2');

-- Component: Outbox 1 (id=3)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox-1', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox-1', 3, 0, 'data');  -- connectors.id: 3
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-outbox-1', 3, 0);                -- pins.id: 3
INSERT INTO event_keys (componentId, channel, key)                    VALUES (3, 'var', 'OUT 1');

-- Component: Outbox 2 (id=4)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox-2', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox-2', 4, 0, 'data');  -- connectors.id: 4
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-outbox-2', 4, 0);                -- pins.id: 4
INSERT INTO event_keys (componentId, channel, key)                    VALUES (4, 'var', 'OUT 2');

-- Component: Outbox 3 (id=5)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox-3', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox-3', 5, 0, 'data');  -- connectors.id: 5
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-outbox-3', 5, 0);                -- pins.id: 5
INSERT INTO event_keys (componentId, channel, key)                    VALUES (5, 'var', 'OUT 3');

-- No Links between pins