package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.EventKeyModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/**
 * DAO for Event Key
 *
 * @author PITSCHR
 */
public interface EventKeyDao {
    /**
     * Returns the total size for all event keys
     *
     * @return size of all event keys
     */
    @SqlQuery("SELECT COUNT(*) FROM event_keys")
    int size();

    /**
     * Returns the {@link EventKeyModel} for given component {@code id}
     *
     * @param id primary key of component
     * @return a {@link EventKeyModel}
     */
    @SqlQuery("SELECT * FROM event_keys WHERE componentId = ?")
    EventKeyModel getByComponentId(final int id);

    /**
     * Inserts a new {@link EventKeyModel} into database
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO event_keys (componentId, channel, key) VALUES (:componentId, :channel, :key)")
    int insert(@BindBean final EventKeyModel model);

    /**
     * Updates an existing {@link EventKeyModel} in database
     *
     * @param model model to be updated
     * @return primary key
     */
    @SqlUpdate("UPDATE event_keys SET channel = :channel, key = :key WHERE id = :id")
    int update(@BindBean final EventKeyModel model);
}
