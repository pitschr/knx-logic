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

package li.pitschmann.knx.api.v1.services;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.Router;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.PinLinksDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.models.PinLinkModel;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UID;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service for link between {@link Pin}
 * <p>
 * Registers/De-registers the links in {@link Router} and persists in the database.
 */
public final class LinkService {
    private final DatabaseManager databaseManager;
    private final Router router;

    public LinkService(final DatabaseManager databaseManager,
                       final Router router) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
        this.router = Objects.requireNonNull(router);
    }

    /**
     * Creates a link between two {@link Pin} to be persisted in the database and registered
     * to the logic router.
     *
     * @param sourcePin the source pin; may not be null
     * @param targetPin the target pin; may not be null
     */
    public void addLink(final Pin sourcePin, final Pin targetPin) {
        Preconditions.checkNonNull(sourcePin, "Source Pin is required.");
        Preconditions.checkNonNull(targetPin, "Target Pin is required.");

        // persists the new link
        final var sourcePinModel = databaseManager.dao(PinsDao.class).find(sourcePin.getUid());
        final var targetPinModel = databaseManager.dao(PinsDao.class).find(targetPin.getUid());
        Preconditions.checkNonNull(sourcePinModel, "Could not find id for source pin '{}'. Not persisted yet?", sourcePin.getUid());
        Preconditions.checkNonNull(targetPinModel, "Could not find id for target pin '{}'. Not persisted yet?", targetPin.getUid());

        final var pinLinkModel = PinLinkModel.create(
                sourcePinModel.getId(),
                targetPinModel.getId()
        );
        databaseManager.dao(PinLinksDao.class).insert(pinLinkModel);

        router.link(sourcePin, targetPin);
    }

    /**
     * Deletes a link pair of two {@link Pin} to be deleted from database and the linked
     * pair of {@link Pin} will be de-registered from the logic router.
     *
     * @param sourcePin the source pin; may not be null
     * @param targetPin the target pin; may not be null
     */
    public void deleteLink(final Pin sourcePin, final Pin targetPin) {
        Preconditions.checkNonNull(sourcePin, "Source Pin is required.");
        Preconditions.checkNonNull(targetPin, "Target Pin is required.");

        // the link between source pin and target pin will be deleted
        databaseManager.dao(PinLinksDao.class).delete(sourcePin.getUid(), targetPin.getUid());

        router.unlink(sourcePin, targetPin);
    }

    /**
     * Deletes all links that are associated with the given {@link Pin}. All links to and from
     * the given {@link Pin} will be deleted from the database and de-registered from the
     * logic router.
     *
     * @param pin the pin; may not be null
     */
    public void deleteLinks(final Pin pin) {
        Preconditions.checkNonNull(pin, "Pin is required.");

        // all links to/from the pin will be deleted
        databaseManager.dao(PinLinksDao.class).delete(pin.getUid());

        router.unlink(pin);
    }

    /**
     * Return list of {@link UID}s that are connected with the given {@link Pin} and registered
     * by the logic router.
     *
     * @param pin the pin; may not be null
     * @return immutable list of {@link UID}s, or empty list if no links could be found.
     */
    public List<UID> getLinkedUIDs(final Pin pin) {
        Preconditions.checkNonNull(pin, "Pin is required.");
        return router.findLinkedPins(pin).stream().map(Pin::getUid).collect(Collectors.toList());
    }
}