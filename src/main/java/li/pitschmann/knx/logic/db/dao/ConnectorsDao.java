package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.uid.UID;
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
public interface ConnectorsDao {
    /**
     * Returns the total size for all connectors
     *
     * @return size of all connectors
     */
    @SqlQuery("SELECT COUNT(*) FROM connectors")
    int size();

    /**
     * Returns the {@link ConnectorModel} for given {@code id}
     *
     * @param id the identifier of connector
     * @return {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE id = ?")
    ConnectorModel find(final int id);

    /**
     * Returns the {@link ConnectorModel} for given {@code uid}
     *
     * @param uid {@link UID} of connector
     * @return {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE uid = ?")
    ConnectorModel find(final UID uid);

    /**
     * Returns all {@link ConnectorModel} for given component {@code id}
     *
     * @param id the identifier of connector
     * @return list of {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE componentId = ?")
    List<ConnectorModel> byComponentId(final int id);

    /**
     * Inserts the {@link ConnectorModel}
     *
     * @param model model of connector
     * @return auto-generated key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO connectors (uid, componentId, bindingType, connectorName) VALUES (:uid, :componentId, :bindingType, :connectorName)")
    int insert(@BindBean final ConnectorModel model);

    /**
     * Updates the {@link ConnectorModel}
     *
     * @param model model of connector
     */
    @SqlUpdate("UPDATE connectors SET uid = :uid, bindingType = :bindingType, connectorName = :connectorName WHERE id = :id")
    void update(@BindBean final ConnectorModel model);

    /**
     * Deletes the {@link ConnectorModel} by id, the associated pins will be also deleted.
     *
     * @param id the model identifier
     */
    @SqlUpdate("DELETE connectors WHERE id = ?")
    int delete(final int id);
}
