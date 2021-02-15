package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.OutboxComponent;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

/**
 * DAO for objects
 *
 * @author PITSCHR
 */
public interface ObjectsDao {
    /**
     * Returns the {@link LogicComponent} for given {@code id}
     *
     * @param id the primary key of component
     * @return an instance of {@link LogicComponent}; or {@code null} if not found
     */
    @SqlQuery("SELECT * FROM components WHERE id = ? AND componentType = 0")
    LogicComponent getLogicComponentById(final int id);

    /**
     * Returns the {@link InboxComponent} for given {@code id}
     *
     * @param id the primary key of component
     * @return an instance of {@link InboxComponent}; or {@code null} if not found
     */
    @SqlQuery("SELECT * FROM components WHERE id = ? AND componentType = 1")
    InboxComponent getInboxComponentById(final int id);

    /**
     * Returns the {@link OutboxComponent} for given {@code id}
     *
     * @param id the primary key of component
     * @return an instance of {@link OutboxComponent}; or {@code null} if not found
     */
    @SqlQuery("SELECT * FROM components WHERE id = ? AND componentType = 2")
    OutboxComponent getOutboxComponentById(final int id);
}
