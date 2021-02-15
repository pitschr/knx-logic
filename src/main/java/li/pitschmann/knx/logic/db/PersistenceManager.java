package li.pitschmann.knx.logic.db;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.db.strategies.PersistenceStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Persistence manager that manages the strategies
 * for persistence of objects to the database.
 *
 * @author PITSCHR
 */
public final class PersistenceManager {
    private static final Logger LOG = LoggerFactory.getLogger(PersistenceManager.class);
    private final Map<Class<?>, PersistenceStrategy<?>> strategies = new HashMap<>();

    /**
     * Adds the {@link PersistenceStrategy}
     *
     * @param persistenceStrategy the strategy implementation for persistence
     */
    public final void addPersistenceStrategy(final PersistenceStrategy<?> persistenceStrategy) {
        strategies.put(persistenceStrategy.compatibleClass(), persistenceStrategy);
        LOG.debug("Persistence Strategy for type '{}' added.", persistenceStrategy.compatibleClass());
    }

    /**
     * Persists the given {@code object} to database. It will look up for the map of
     * {@link PersistenceStrategy}.
     * <p>
     * If a suitable persistence strategy was found, then proceed with saving, otherwise
     * {@link IllegalArgumentException} is thrown.
     *
     * @param object the object that should be persisted; may not be null
     * @return result from {@link PersistenceStrategy}, otherwise {@link IllegalArgumentException}
     */
    public <T> int save(final T object) {
        final var strategy = strategies.get(object.getClass());
        Preconditions.checkArgument(strategy != null, "Could not find persistence strategy for: {}", object.getClass());

        // we know that it is safe to be casted
        @SuppressWarnings("unchecked")
        final var strategyCasted = (PersistenceStrategy<T>) strategy;

        return strategyCasted.save(object);
    }

}
