package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'pins' table
 *
 * @author PITSCHR
 */
public interface PinsDao {
    /**
     * Returns the total size for all pins
     *
     * @return the total size of pins
     */
    @SqlQuery("SELECT COUNT(*) FROM pins")
    int size();

    /**
     * Returns the {@link PinModel} for given {@code id}
     *
     * @param id the identifier of pin model
     * @return {@link PinModel}
     */
    @SqlQuery("SELECT * FROM pins WHERE id = ?")
    PinModel find(final int id);

    /**
     * Returns the {@link PinModel} for given {@code uid}
     *
     * @param uid the {@link UID} of pin model
     * @return {@link PinModel}
     */
    @SqlQuery("SELECT * FROM pins WHERE uid = ?")
    PinModel find(final UID uid);

    /**
     * Returns all {@link PinModel} for given connector {@code id} and order by it's index
     *
     * @param connectorId the id of connector
     * @return list of {@link PinModel} ordered by index
     */
    @SqlQuery("SELECT * FROM pins WHERE connectorId = ? ORDER BY index")
    List<PinModel> getByConnectorId(final int connectorId);

    /**
     * Inserts the {@link PinModel}
     *
     * @param model the pin model to be persisted
     * @return auto-generated key from {@code pins} table
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO pins (uid, connectorId, index) VALUES (:uid, :connectorId, :index)")
    int insert(@BindBean final PinModel model);

    /**
     * Insert a list of {@link PinModel}
     *
     * @param models list of pin model to be persisted
     * @return array of auto-generated keys from {@code pins} table
     */
    @GetGeneratedKeys
    @SqlBatch("INSERT INTO pins (uid, connectorId, index) VALUES (:uid, :connectorId, :index)")
    int[] insert(@BindBean final List<PinModel> models);

    /**
     * Deletes the {@link PinModel} by primary key
     *
     * @param id the identifier of pin model
     */
    @SqlUpdate("DELETE FROM pins WHERE id = ?")
    void delete(final int id);

    /**
     * Updates the Index of {@link PinModel} by primary key
     *
     * @param id       the identifier of pin model to be updated
     * @param newIndex the new index value
     */
    @SqlUpdate("UPDATE pins SET index = :newIndex WHERE id = :id")
    void updateIndex(final @Bind("id") int id, final @Bind("newIndex") int newIndex);
}
