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

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import org.jdbi.v3.sqlobject.transaction.Transaction;

import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract {@link PersistenceStrategy} implementation for {@link Component}
 *
 * @author PITSCHR
 */
abstract class AbstractComponentPersistenceStrategy<T extends Component> implements PersistenceStrategy<T> {
    private static final Lock LOCK = new ReentrantLock();
    protected final DatabaseManager databaseManager;

    protected AbstractComponentPersistenceStrategy(final DatabaseManager databaseManager) {
        this.databaseManager = Objects.requireNonNull(databaseManager);
    }

    /**
     * Inserts or updates the {@link Component}.
     *
     * @param component the component to be persisted; may not be null
     */
    @Transaction
    @Override
    public final int save(final T component) {
        Objects.requireNonNull(component);
        LOCK.lock();
        try {
            return databaseManager.jdbi().inTransaction(h -> {
                final var dao = databaseManager.dao(ComponentsDao.class);
                var model = dao.find(component.getUid());
                return model == null ? insert(component) : update(model, component);
            });
        } finally {
            LOCK.unlock();
        }
    }

    /**
     * Persists given {@link Component} to the database
     *
     * @param component component as source to create a model and insert it
     * @return the new id of component
     */
    protected abstract int insert(final T component);

    /**
     * Persists given {@link Component} to the database
     *
     * @param model     model to be updated
     * @param component component as source to update the model
     * @return the new primary key of component
     */
    protected abstract int update(final ComponentModel model, final T component);
}
