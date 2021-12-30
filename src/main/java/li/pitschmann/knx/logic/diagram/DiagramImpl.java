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

package li.pitschmann.knx.logic.diagram;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;

/**
 * Implementation of {@link Diagram} that represents
 * how the (input, logic, output, ...) components are linked.
 *
 * @author PITSCHR
 */
public final class DiagramImpl implements Diagram {
    private UID uid = UIDFactory.createRandomUid();
    private String name;
    private String description;

    @Override
    public UID getUid() {
        return uid;
    }

    // TODO: this method should be package-protected --> implement it by laoder like "LogicComponentLoader"
    public void setUid(UID uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("uid", uid) //
                .add("name", name) //
                .add("description", description) //
                .toString();
    }
}
