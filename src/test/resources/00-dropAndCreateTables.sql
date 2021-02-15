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
    componentType TINYINT      NOT NULL, -- 0=logic, 1=inbox, 2=outbox
    uid           VARCHAR(100) NOT NULL,
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
    componentId        INT          NOT NULL,
    bindingType        TINYINT      NOT NULL, -- 0=static, 1=dynamic
    connectorName      VARCHAR(100) NOT NULL, -- unique name of connector
    creationTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (componentId) REFERENCES components (id) ON DELETE CASCADE,
    CONSTRAINT connectors_unique UNIQUE (componentId, connectorName)
);

--
-- PINS
--
CREATE TABLE pins
(
    id            INT          AUTO_INCREMENT PRIMARY KEY,
    connectorId   INT          NOT NULL,
    uid           VARCHAR(100) NOT NULL UNIQUE,
    index         TINYINT      NOT NULL DEFAULT 0, -- used for dynamic only
    creationTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (connectorId) REFERENCES connectors (id) ON DELETE CASCADE,
    CONSTRAINT pins_unique UNIQUE (connectorId, index),
    CONSTRAINT pins_unique_2 UNIQUE (uid)
);

--
-- PINS HISTORY
--
CREATE TABLE pins_history
(
    id            INT            AUTO_INCREMENT PRIMARY KEY,
    pinId         INT            NOT NULL,
    value         VARCHAR(4000),
    valueType     VARCHAR(255),
    creationTs    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pinId) REFERENCES pins (id) ON DELETE CASCADE
);

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
CREATE TABLE diagram -- other names: logic_scheme
(
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description VARCHAR(4000),
    creationTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT diagram_uq UNIQUE (name)
);

--
-- LOGIC DIAGRAM <--> COMPONENTS
--
CREATE TABLE diagram_components
(
    id          INT            AUTO_INCREMENT PRIMARY KEY,
    componentId INT            NOT NULL,
    layoutId    INT            NOT NULL,
    position_x  INT            NOT NULL DEFAULT 0,
    position_y  INT            NOT NULL DEFAULT 0,
    creationTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (layoutId) REFERENCES diagram (id) ON DELETE CASCADE,
    FOREIGN KEY (componentId) REFERENCES components (id) ON DELETE CASCADE,
    CONSTRAINT diagram_components_uq UNIQUE (componentId)
);

--
-- LOGIC DIAGRAM LINKS (SOURCE PIN <--> TARGET PIN)
--
CREATE TABLE diagram_links
(
    id             INT            AUTO_INCREMENT PRIMARY KEY,
    sourceUid      INT            NOT NULL,
    targetUid      INT            NOT NULL,
    svg_path_data  VARCHAR(500)   NOT NULL,  -- format: M 100 100 h 80 v80 ...
    creationTs     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    modifiedTs     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sourceUid) REFERENCES pins (id) ON DELETE CASCADE,
    FOREIGN KEY (targetUid) REFERENCES pins (id) ON DELETE CASCADE,
    CONSTRAINT diagram_links_uq UNIQUE (sourceUid, targetUid)
)
