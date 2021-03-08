INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic', 0, 'test.components.logic.ThroughputLogic');

INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-input',  1, 0, 'input'),  -- connectors.id: 1
    ('uid-connector-output', 1, 0, 'output'); -- connectors.id: 2

INSERT INTO pins (uid, connectorId) VALUES
    ('uid-pin-input',  1), -- pins.id: 1
    ('uid-pin-output', 2); -- pins.id: 2

INSERT INTO pin_values(pinId, value, valueType) VALUES
    (1, '123456789012345678901234567890123456789012345678901234567890.0987654321', 'java.math.BigDecimal'),
    (2, 'I am a string!', 'java.lang.String');
