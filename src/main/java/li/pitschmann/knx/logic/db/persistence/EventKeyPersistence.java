/*
 * Copyright (C) 2021 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.logic.db.persistence;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.EventKeyModel;
import li.pitschmann.knx.logic.event.EventKey;

import java.util.Objects;

/**
 * Persist the {@link EventKey} to {@link EventKeyModel} in database
 *
 * @author PITSCHR
 */
class EventKeyPersistence {
    private final DatabaseManager databaseManager;

    EventKeyPersistence(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    /**
     * Persists given {@link EventKey} to the database
     *
     * @param componentId the id of component that is used as owner for connector; may not be zero or negative
     * @param eventKey    the event key to be persisted; may not be null
     * @return the new primary key of event key
     */
    int insertEventKey(final int componentId,
                       final EventKey eventKey) {
        Preconditions.checkArgument(componentId > 0,
                "Component ID must be positive, but was: {}", componentId);
        final var eventKeyModel = toModel(componentId, eventKey);
        return databaseManager.dao(EventKeyDao.class).insert(eventKeyModel);
    }

    /**
     * Updates the given {@link EventKey} to the database
     *
     * @param componentId the identifier of {@link ComponentModel} as owner for {@link EventKey}
     * @param eventKey    the event key to be updated; may not be null
     */
    void updateEventKey(final int componentId,
                        final EventKey eventKey) {
        Preconditions.checkArgument(componentId > 0,
                "Component ID must be positive, but was: {}", componentId);
        final var eventKeyModel = toModel(componentId, eventKey);
        databaseManager.dao(EventKeyDao.class).update(eventKeyModel);
    }

    /**
     * Creates a new {@link EventKeyModel} based on {@code componentId}
     * and {@link EventKey} data
     *
     * @param componentId the identifier of component in database
     * @param eventKey    the event key that is subject to be persisted in the database; may not be null
     * @return a new {@link EventKeyModel}
     */
    private EventKeyModel toModel(final int componentId, final EventKey eventKey) {
        return EventKeyModel.builder()
                .componentId(componentId)
                .channel(eventKey.getChannel())
                .key(eventKey.getIdentifier())
                .build();
    }
}
