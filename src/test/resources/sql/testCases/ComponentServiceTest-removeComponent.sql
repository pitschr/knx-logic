--
--  .---------.    .---------.     .---------.
--  |   IN    |    |   NOT   |     |   OUT   |
--  `---------´    `---------´     `---------´
--
--
-- -----------------------------------------------------------------
--  Components
-- -----------------------------------------------------------------
-- Component: Inbox (components.id: 1)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-inbox', 1, 'li.pitschmann.knx.logic.components.inbox.VariableInbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-inbox', 1, 0, 'data');     -- connectors.id: 1
INSERT INTO pins       (uid, connectorId, index)                      VALUES ('uid-pin-inbox', 1, 0);                   -- pins.id: 1
INSERT INTO event_keys (componentId, channel, key)                    VALUES (1, 'var', 'IN');

-- Component: Not Logic (components.id: 2)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-logic', 0, 'test.components.logic.NegationLogic');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-logic-input',  2, 0, 'input' ),                                                                     -- connectors.id: 2
    ('uid-connector-logic-output', 2, 1, 'output');                                                                     -- connectors.id: 3
INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-pin-logic-input',  2, 0),                                                                                     -- pins.id: 2
    ('uid-pin-logic-output', 3, 0);                                                                                     -- pins.id: 3

-- Component: Outbox (component.id: 3)
INSERT INTO components (uid, componentType, className)                VALUES ('uid-component-outbox', 2, 'li.pitschmann.knx.logic.components.outbox.VariableOutbox');
INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES ('uid-connector-outbox', 3, 0, 'data');    -- connectors.id: 4
INSERT INTO pins (uid, connectorId, index)                            VALUES ('uid-pin-outbox', 4, 0);                  -- pins.id: 4
INSERT INTO event_keys (componentId, channel, key)                    VALUES (3, 'var', 'OUT');
