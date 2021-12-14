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

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.models.DiagramModel;
import li.pitschmann.knx.logic.db.models.Model;
import li.pitschmann.knx.logic.diagram.Diagram;
import li.pitschmann.knx.logic.uid.UIDAware;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Persist the {@link Diagram} to {@link DiagramModel} in database
 *
 * @author PITSCHR
 */
public abstract class AbstractPersistence<M extends Model, T extends UIDAware> implements PersistenceStrategy<T> {
    protected final DatabaseManager databaseManager;
    private final Lock lock = new ReentrantLock();

    protected AbstractPersistence(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    /**
     * Persists given {@link Object} to the database
     *
     * @param object the object to be persisted; may not be null
     * @return the new primary key of object
     */
    protected abstract int insert(final T object);

    /**
     * Updates the given {@link Object} to the database
     *
     * @param model  the model that is subject to be updated
     * @param object the source of object to update the model; may not be null
     */
    abstract void update(final M model, final T object);

    /**
     * Inserts or updates the given object
     *
     * @param object the object to be persisted; may not be null
     */
    @Transaction
    @Override
    public int save(final T object) {
        Objects.requireNonNull(object);
        lock.lock();
        try {
            return databaseManager.jdbi().inTransaction(h -> {
                var model = findModel(object);
                if (model == null) {
                    return insert(object);
                } else {
                    update(model, object);
                    return model.getId();
                }
            });
        } finally {
            lock.unlock();
        }
    }

    /**
     * Find a {@link Model} based on the given object
     *
     * @param object the object to be used for look-up; may not be null
     * @return the existing model; or {@code null} if not found
     */
    @Nullable
    protected abstract M findModel(final T object);

    /**
     * Creates a new {@link Model} instance based on the given object
     *
     * @param object the object as source for model
     * @return a new {@link Model}
     */
    protected abstract M toModel(final T object);
}
