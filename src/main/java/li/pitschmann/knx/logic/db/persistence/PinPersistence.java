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
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Persist the {@link Pin} to {@link PinModel} in database
 *
 * @author PITSCHR
 */
class PinPersistence {
    private final DatabaseManager databaseManager;

    PinPersistence(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    /**
     * Saves the list of {@link Pin} for given connector id
     *
     * @param connectorId the primary key of connector in database
     * @param pins        list of {@link Pin} to be saved
     */
    public void insertPins(final int connectorId, final List<Pin> pins) {
        final var pinModels = new ArrayList<PinModel>(pins.size());
        for (final var pin : pins) {
            pinModels.add(toModel(connectorId, pin));
        }
        databaseManager.dao(PinsDao.class).insert(pinModels);
    }

    /**
     * Creates a new {@link PinModel} based on {@code connectorId}
     * and {@link Pin} data
     *
     * @param connectorId the identifier of connector in database
     * @param pin         the pin that is subject to be persisted in the database; may not be null
     * @return a new {@link PinModel}
     */
    private PinModel toModel(final int connectorId, final Pin pin) {
        Preconditions.checkArgument(connectorId > 0,
                "Connector ID must be positive, but was: {}", connectorId);

        // index for dynamic may vary, for static it is always ZERO
        final var index = pin instanceof DynamicPin ? ((DynamicPin) pin).getIndex() : 0;

        return PinModel.builder()
                .connectorId(connectorId)
                .uid(pin.getUid())
                .index(index)
                .build();
    }
}
