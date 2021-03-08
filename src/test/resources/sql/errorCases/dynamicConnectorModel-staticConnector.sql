--
-- Test with wrong SQL data:
--
-- Model Connector is dynamic
--   BindingType#STATIC (ordinal = 0)
--   BindingType#DYNAMIC (ordinal = 1)
--
INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-static', 1, 'li.pitschmann.knx.logic.components.inbox.DPT1Inbox'); -- component: 1

INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-static', 1, 1, 'barName');  -- connector: 1

INSERT INTO pins (uid, connectorId, index) VALUES
    ('uid-static-1', 1, 0), -- pin: 1
    ('uid-static-2', 1, 1), -- pin: 2
    ('uid-static-3', 1, 2); -- pin: 3
