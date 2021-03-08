INSERT INTO components (uid, componentType, className) VALUES
    ('uid-component-logic', 0, 'test.components.logic.IncrementLogic');

INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES
    ('uid-connector-input',  1, 0, 'input'),  -- connectors.id: 1
    ('uid-connector-output', 1, 0, 'output'); -- connectors.id: 2

INSERT INTO pins (uid, connectorId) VALUES
    ('uid-pin-input',  1), -- pins.id: 1
    ('uid-pin-output', 2); -- pins.id: 2

INSERT INTO pin_values(pinId, value, valueType) VALUES
    (1, '4711', 'java.lang.Integer'),
    (2, '1477', 'java.lang.Integer'),
    (1, '6337', 'java.lang.Integer'),
    (1, '8312', 'java.lang.Integer'),
    (2, '7717', 'java.lang.Integer'),   -- this is the last one for pins.id: 2
    (1, '9332', 'java.lang.Integer'),
    (1, '4921', 'java.lang.Integer');   -- this is the last one for pins.id: 1
