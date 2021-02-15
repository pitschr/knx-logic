package li.pitschmann.knx.logic.db.jdbi.mappers.row;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.jdbi.v3.core.statement.StatementContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Database mapper to create {@link LogicComponent} out from
 * {@code COMPONENTS} database table.
 * <p>
 * This class will reads following database tables:
 * <ul>
 * <li>components</li>
 * <li>connectors</li>
 * <li>pins</li>
 * </ul>
 *
 * @author PITSCHR
 */
public class LogicComponentMapper extends AbstractComponentMapper<LogicComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(LogicComponentMapper.class);

    public LogicComponentMapper(final DatabaseManager databaseManager) {
        super(databaseManager);
    }

    @Override
    public LogicComponent map(final ResultSet rs, final StatementContext ctx) throws SQLException {
        final var id = rs.getInt("id");
        final var className = rs.getString("className");
        final var uid = UIDFactory.createUid(rs.getString("uid"));

        // create new component, we will update/wrap in next steps
        final Logic logic = loadClassAndCast(className, Logic.class);
        final var component = new LogicComponentImpl(logic);

        // set previous UID to make sure that we recognize it as same component
        component.setUid(uid);

        // update connectors (and pins inside)
        final var connectorModels = databaseManager.dao(ConnectorsDao.class).getByComponentId(id);
        if (connectorModels.isEmpty()) {
            LOG.warn("No connectors found for logic component model?!?! Please double-check your component (id={}): {}", id, className);
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
