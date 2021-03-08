--
-- DROP all TABLES, CONSTRAINTS, ...
--
DROP ALL OBJECTS;

--
-- COMPONENTS
--
CREATE TABLE components
(
    id            INT          AUTO_INCREMENT PRIMARY KEY,
    uid           VARCHAR(100) NOT NULL DEFAULT CAST(RANDOM_UUID() AS VARCHAR),
    componentType TINYINT      NOT NULL, -- 0=logic, 1=inbox, 2=outbox
    className     VARCHAR(500) NOT NULL,
    creationTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT components_unique UNIQUE (uid)
);

--
-- CONNECTORS
--
CREATE TABLE connectors
(
    id                 INT          AUTO_INCREMENT PRIMARY KEY,
    uid                VARCHAR(100) NOT NULL DEFAULT CAST(RANDOM_UUID() AS VARCHAR),
    componentId        INT          NOT NULL,
    bindingType        TINYINT      NOT NULL, -- 0=static, 1=dynamic
    connectorName      VARCHAR(100) NOT NULL, -- unique name of connector
    creationTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (componentId) REFERENCES components (id) ON DELETE CASCADE,
    CONSTRAINT connectors_unique UNIQUE (componentId, connectorName),
    CONSTRAINT connectors_unique_2 UNIQUE (uid)
);

--
-- PINS
--
CREATE TABLE pins
(
    id            INT          AUTO_INCREMENT PRIMARY KEY,
    uid           VARCHAR(100) NOT NULL DEFAULT CAST(RANDOM_UUID() AS VARCHAR),
    connectorId   INT          NOT NULL,
    index         TINYINT      NOT NULL DEFAULT 0, -- used for dynamic only
    creationTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (connectorId) REFERENCES connectors (id) ON DELETE CASCADE,
    CONSTRAINT pins_unique UNIQUE (connectorId, index),
    CONSTRAINT pins_unique_2 UNIQUE (uid)
);

--
-- PINS LINKS
--
CREATE TABLE pin_links
(
    id            INT          AUTO_INCREMENT PRIMARY KEY,
    pin1          INT          NOT NULL,
    pin2          INT          NOT NULL,
    creationTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (pin1) REFERENCES pins (id) ON DELETE CASCADE,
    FOREIGN KEY (pin2) REFERENCES pins (id) ON DELETE CASCADE,
    CONSTRAINT pin_links_unique UNIQUE (pin1, pin2),
    CONSTRAINT pin_links_integrity CHECK (pin1 < pin2)  -- ensures that pin1 id has lower value than pin2 id
);

--
-- HISTORY for pins
--
CREATE TABLE pin_values
(
    id            INT            AUTO_INCREMENT PRIMARY KEY,
    pinId         INT            NOT NULL,
    value         VARCHAR(4000),
    valueType     VARCHAR(255),
    creationTs    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pinId) REFERENCES pins (id) ON DELETE CASCADE
);
CREATE INDEX pin_values_desc_index ON pin_values (pinId, id DESC);

--
-- EVENT KEYS
--
CREATE TABLE event_keys
(
    id                 INT          AUTO_INCREMENT PRIMARY KEY,
    componentId        INT          NOT NULL,
    channel            VARCHAR(10)  NOT NULL,
    key                VARCHAR(30)  NOT NULL,
    creationTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (componentId) REFERENCES components (id) ON DELETE CASCADE,
    CONSTRAINT event_keys_unique UNIQUE (componentId, channel, key)
);


--
-- LOGIC DIAGRAM
--
CREATE TABLE diagrams -- other names: logic_scheme
(
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(4000),
    creationTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT diagram_unique UNIQUE (name)
);

--
-- LOGIC DIAGRAM <--> COMPONENTS
--
CREATE TABLE diagram_components
(
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    diagramId   INT            NOT NULL,
    componentId INT            NOT NULL,
    positionX   INT            NOT NULL DEFAULT 0,
    positionY   INT            NOT NULL DEFAULT 0,
    creationTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (diagramId) REFERENCES diagrams (id) ON DELETE CASCADE,
    FOREIGN KEY (componentId) REFERENCES components (id) ON DELETE CASCADE,
    CONSTRAINT diagram_components_unique UNIQUE (componentId)
);

--
-- LOGIC DIAGRAM LINKS (SOURCE PIN <--> TARGET PIN)
--
CREATE TABLE diagram_links
(
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    diagramId      INT            NOT NULL,
    pinLinkId      INT            NOT NULL,
    svgPath        VARCHAR(500)   NOT NULL,  -- format: M 100 100 h 80 v80 ...
    creationTs     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (diagramId) REFERENCES diagrams (id) ON DELETE CASCADE,
    FOREIGN KEY (pinLinkId) REFERENCES pin_links (id) ON DELETE CASCADE,
    CONSTRAINT diagram_links_unique UNIQUE (diagramId, pinLinkId)
);
