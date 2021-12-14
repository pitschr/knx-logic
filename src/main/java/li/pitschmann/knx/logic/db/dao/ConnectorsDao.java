package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'connectors' table
 *
 * @author PITSCHR
 */
public interface ConnectorsDao extends GenericDao<ConnectorModel> {
    @Override
    @SqlQuery("SELECT COUNT(*) FROM connectors")
    int size();

    @Override
    default List<ConnectorModel> all() {
        throw new UnsupportedOperationException("No all() supported for ConnectorModel");
    }

    @Override
    @SqlQuery("SELECT * FROM connectors WHERE uid = ?")
    ConnectorModel find(final UID uid);

    @Override
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO connectors (uid, componentId, bindingType, connectorName) " +
            "   VALUES (:uid, :componentId, :bindingType, :connectorName)")
    int insert(@BindBean final ConnectorModel model);

    @Override
    @SqlUpdate("UPDATE connectors SET " +
            "     uid = :uid, " +
            "     bindingType = :bindingType, " +
            "     connectorName = :connectorName " +
            "   WHERE id = :id")
    void update(final @Bind("id") int id, @BindBean final ConnectorModel model);

    @Override
    @SqlUpdate("DELETE connectors WHERE uid = ?")
    void delete(final UID uid);

    /**
     * Returns the {@link ConnectorModel} for given {@code id}
     *
     * @param id the identifier of connector
     * @return {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE id = ?")
    ConnectorModel find(final int id);

    /**
     * Returns all {@link ConnectorModel} for given component {@code id}
     *
     * @param id the identifier of connector
     * @return list of {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE componentId = ?")
    List<ConnectorModel> byComponentId(final int id);

}
