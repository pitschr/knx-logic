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

package li.pitschmann.knx.logic.db.jdbi.mappers.data;

import li.pitschmann.knx.logic.uid.UID;

/**
 * A 2-tuple containing {@link UID}s
 */
public class TwoTupleUID {
    private final UID uid1;
    private final UID uid2;

    public TwoTupleUID(final UID uid1, final UID uid2) {
        this.uid1 = uid1;
        this.uid2 = uid2;
    }

    public UID getUid1() {
        return uid1;
    }

    public UID getUid2() {
        return uid2;
    }
}
