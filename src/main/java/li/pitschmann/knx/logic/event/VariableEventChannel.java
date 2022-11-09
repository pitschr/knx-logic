/*
 * Copyright (C) 2022 Pitschmann Christoph
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

package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.utils.Maps;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Channel for Variable events
 *
 * @author PITSCHR
 */
public final class VariableEventChannel implements EventChannel {
    public static final String CHANNEL_ID = "var";
    private final Map<String, Object> variableMap = Maps.newHashMap(100);

    public static EventKey createKey(final String name) {
        return new EventKey(CHANNEL_ID, name);
    }

    @Override
    public String getChannel() {
        return CHANNEL_ID;
    }

    /**
     * Returns the cached value for given variable name
     *
     * @param name the name of variable as key to be look up in the variable map
     * @return the cached value or
     * @throws NoSuchElementException if no data could be found for given variable name
     */
    public Object getCachedValueFor(final String name) {
        final var data = variableMap.get(name);
        if (data == null) {
            throw new NoSuchElementException("Could not find data for variable name: " + name);
        }
        return data;
    }

    @Override
    public void outbound(final Event event) {
        variableMap.put(
                event.getKey().getIdentifier(), //
                event.getData() //
        );
    }
}
