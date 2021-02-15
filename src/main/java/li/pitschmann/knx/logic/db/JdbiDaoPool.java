package li.pitschmann.knx.logic.db;

import org.jdbi.v3.core.Jdbi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Pool for {@link Jdbi} DAO to speed up the performance
 * <p>
 * This JDBI Dao Pool is package-protected and should be called by
 * an implementation of {@link DatabaseManager} only.
 *
 * @author PITSCHR
 */
final class JdbiDaoPool {
    private static final Map<Class<?>, Object> POOL = new HashMap<>();
    private final DatabaseManager databaseManager;
    private final Lock lock = new ReentrantLock();

    JdbiDaoPool(final DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
    }

    /**
     * Returns DAO from {@link Jdbi}
     *
     * @param clazz the class of DAO
     * @return the DAO instance
     */
    @SuppressWarnings("unchecked")
    public <E> E getDaoOnDemand(final Class<E> clazz) {
        final var obj = POOL.get(clazz);
        final E dao;
        if (obj == null) {
            lock.lock();
            try {
                dao = (E) POOL.computeIfAbsent(clazz, f -> databaseManager.jdbi().onDemand(f));
            } finally {
                lock.unlock();
            }
        } else {
            dao = (E) obj;
        }
        return dao;
    }

    /**
     * Clears the pool
     */
    public void clear() {
        lock.lock();
        try {
            POOL.clear();
        } finally {
            lock.unlock();
        }
    }
}
