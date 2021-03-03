INSERT INTO components (componentType, className, uid) VALUES
    (0, 'test.components.logic.IncrementLogic', 'uid-component-logic');

INSERT INTO connectors (componentId, bindingType, connectorName) VALUES
    (1, 0, 'input'),         -- connectors.id: 1
    (1, 0, 'output');        -- connectors.id: 2

INSERT INTO pins (connectorId, uid) VALUES
    (1, 'uid-pin-input'),    -- pins.id: 1
    (2, 'uid-pin-output');   -- pins.id: 2

INSERT INTO pin_values(pinId, value, valueType) VALUES
    (1, '4711', 'java.lang.Integer'),
    (2, '1477', 'java.lang.Integer');
