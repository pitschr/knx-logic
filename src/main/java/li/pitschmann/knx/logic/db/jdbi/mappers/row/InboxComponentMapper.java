package li.pitschmann.knx.logic.db.jdbi.mappers.row;

import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.event.EventKey;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Database mapper to create {@link InboxComponent} out from
 * {@code COMPONENTS} database table.
 * <p>
 * This class will reads following database tables:
 * <ul>
 * <li>components</li>
 * <li>connectors</li>
 * <li>pins</li>
 * </ul>
 */
public class InboxComponentMapper extends AbstractComponentMapper<InboxComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(InboxComponentMapper.class);

    public InboxComponentMapper(final DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public InboxComponent map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        final var id = rs.getInt("id");
        final var className = rs.getString("className");
        final var uid = UIDFactory.createUid(rs.getString("uid"));

        // create event key
        final var eventKeyModel = Objects.requireNonNull(databaseManager.dao(EventKeyDao.class).getByComponentId(id));
        final var eventKey = new EventKey(eventKeyModel.getChannel(), eventKeyModel.getKey());

        // create new component, we will update/wrap in next steps
        final Inbox inbox = loadClassAndCast(className, Inbox.class);
        final var component = new InboxComponentImpl(eventKey, inbox);

        // set previous UID to make sure that we recognize it as same component
        component.setUid(uid);

        // update connectors (and pins inside)
        final var connectorModels = databaseManager.dao(ConnectorsDao.class).getByComponentId(id);
        if (connectorModels.isEmpty()) {
            LOG.warn("No connectors found for inbox component model?!?! Please double-check your component (id={}): {}", id, className);
        } else if (LOG.isDebugEnabled()) {
            connectorModels.forEach(c -> LOG.debug("{}@{}: {}@{}",
                    className, //
                    c.getComponentId(), //
                    c.getConnectorName(), //
                    c.getId()) //
            );
        }
        updateConnectors(component.getConnectors(), connectorModels);

        return component;
    }
}
