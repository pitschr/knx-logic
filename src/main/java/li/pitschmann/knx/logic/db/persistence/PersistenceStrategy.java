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

/**
 * Strategy for database persistence
 *
 * @param <T> type of object to be stored
 * @author PITSCHR
 */
public interface PersistenceStrategy<T> {
    /**
     * Saves the object to database
     *
     * @param object object to be stored
     * @return the primary key of {@code T}
     */
    int save(T object);

    /**
     * Returns array of compatible classes that would be suitable
     * for the current {@link PersistenceStrategy} implementation
     *
     * @return array of {@link Class} that is considered as compatible for persistence strategy
     */
    Class<?>[] compatibleClasses();
}
