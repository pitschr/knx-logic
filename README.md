[![Build Status](https://github.com/pitschr/knx-logic/workflows/build/badge.svg?branch=main)](https://github.com/pitschr/knx-logic/actions)
[![Coverage Status](https://coveralls.io/repos/github/pitschr/knx-logic/badge.svg?branch=main)](https://coveralls.io/github/pitschr/knx-logic?branch=main)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)
[![Maven Central](https://img.shields.io/maven-central/v/li.pitschmann/knx-logic.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22li.pitschmann%22)

# KNX Logic Engine

... planned ...


| Method | API Endpoint | Description |
| ------ | ------------ | ----------- |
| GET | `/logic/v1/links/:uid` | Returns array of all Pin UIDs that is linked in any way with the given *Pin UID* |
| DELETE | `/logic/v1/links/:uid` | Delete all links to the given *Pin UID* |
| PUT | `/logic/v1/links/:uid1/:uid2` | Registers one specific link from *Pin UID* to another *Pin UID*. The direction of link is not relevant, *uid1=>uid2* and *uid2=>uid1* have same meaning. |
| DELETE | `/logic/v1/links/:uid1/:uid2` | Delete one specific link between *Pin UID* and another *Pin UID*. The UID of pin is interchangeable. |

| Method | API Endpoint | Description |
| ------ | ------------ | ----------- |
| GET | `/logic/v1/diagrams` | Returns array of all available diagrams |
| POST | `/logic/v1/diagrams` | Creates and registers a new diagram |
| GET | `/logic/v1/diagrams/:uid`| Returns an existing diagram by UID |
| PATCH | `/logic/v1/diagrams/:uid` | Updates an existing diagram by UID |
| DELETE | `/logic/v1/diagrams/:uid` | Deletes an existing diagram by UID |

| Method | API Endpoint | Description |
| ------ | ------------ | ----------- |
| GET | `/logic/v1/components` | Returns array of all available components |
| POST | `/logic/v1/components` | Creates a new component (logic, inbox or output) |
| GET | `/logic/v1/components/:uid` | Returns an existing component by UID |
| DELETE | `/logic/v1/components/:uid` | Deletes an existing component by UID |

| Method | API Endpoint | Description |
| ------ | ------------ | ----------- |
| GET | `/logic/v1/connectors/:uid` | Returns an existing connector by UID |
| POST | `/logic/v1/connectors/:uid/pins` | Adds a new pin to connector UID at the *last index*. Works only for dynamic connector |
| POST | `/logic/v1/connectors/:uid/pins/:index` | Adds a new pin to connector UID on *N-th index*. Works only for dynamic connector |
| DELETE | `/logic/v1/connectors/:uid/pins/:index` | Deletes an existing pin on *n-th index* owned by the connector UID. Works only for dynamic connector |

| Method | API Endpoint | Description |
| ------ | ------------ | ----------- |
| GET | `/logic/v1/pins/:uid` | Returns an existing pin by UID |
| GET | `/logic/v1/pins/:uid/value` | Returns the current value of existing pin UID |
| POST | `/logic/v1/pins/:uid/value` | Sets a new value of the existing pin UID. Works only for *input pin* |
