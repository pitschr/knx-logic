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

package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.logic.db.models.Model;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;

import java.util.List;

public interface GenericDao<M extends Model> {

    /**
     * Returns the total size for all models which is a type
     * of {@code M}
     *
     * @return the total size
     */
    int size();

    /**
     * Returns all models of type {@code M}
     *
     * @return list of models
     */
    List<M> all();

    /**
     * Returns the model of type {@code M} for given {@link UID}
     *
     * @param uid {@link UID} of model
     * @return the model; or null if not found
     */
    @Nullable
    M find(final UID uid);

    /**
     * Persists a new model of type {@code M} to databse
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    int insert(@BindBean final M model);

    /**
     * Updates an existing model of type {@code M} in database
     *
     * @param model model to be updated
     */
    void update(@Bind("id") final int id, @BindBean final M model);

    /**
     * Deletes an existing model of type {@code M} from database
     *
     * @param uid {@link UID} of model of model to be deleted
     */
    void delete(final UID uid);
}
