package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ConnectorModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for connectors
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
     * Returns the connector for given {@code id}
     *
     * @param id the id of connector to be returned
     * @return {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE id = ?")
    ConnectorModel getById(final int id);

    /**
     * Returns all connectors for given component {@code id}
     *
     * @param id
     * @return list of {@link ConnectorModel}
     */
    @SqlQuery("SELECT * FROM connectors WHERE componentId = ?")
    List<ConnectorModel> getByComponentId(final int id);

    /**
     * Inserts the {@link ConnectorModel}
     *
     * @param model
     * @return auto-generated key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO connectors (componentId, bindingType, connectorName) VALUES (:componentId, :bindingType, :connectorName)")
    int insert(@BindBean final ConnectorModel model);

    /**
     * Updates the {@link ConnectorModel}
     *
     * @param model
     */
    @SqlUpdate("UPDATE connectors SET bindingType = :bindingType, connectorName = :connectorName WHERE id = :id")
    void update(@BindBean final ConnectorModel model);

    /**
     * Deletes the {@link ConnectorModel} by id, the associated pins will be also deleted.
     *
     * @param id the id of connector to be deleted
     */
    @SqlUpdate("DELETE connectors WHERE id = ?")
    int delete(final int id);
}
