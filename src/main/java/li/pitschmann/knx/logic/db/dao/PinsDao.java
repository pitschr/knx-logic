package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.jdbi.mappers.row.PinValueMapper;
import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

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
     * @return
     */
    @SqlQuery("SELECT COUNT(*) FROM pins")
    int size();

    /**
     * Returns the {@link PinModel} for given {@code id}
     *
     * @param id the primary key of pin
     * @return {@link PinModel}
     */
    @SqlQuery("SELECT * FROM pins WHERE id = ?")
    PinModel getById(final int id);

    /**
     * Returns the component for given {@code uid}
     *
     * @param uid {@link UID} of pin
     * @return {@link PinModel}
     */
    @SqlQuery("SELECT * FROM pins WHERE uid = ?")
    PinModel getByUid(final UID uid);

    /**
     * Returns all {@link PinModel} for given connector {@code id} and order by it's index
     *
     * @param connectorId the id of connector
     * @return list of {@link PinModel} ordered by index
     */
    @SqlQuery("SELECT * FROM pins WHERE connectorId = ? ORDER BY index")
    List<PinModel> getByConnectorId(final int connectorId);

    /**
     * Returns the last value by pin id from {@code pins_history} table
     *
     * @param id the id of pin
     * @return the last value
     */
    @SqlQuery("SELECT value, valueType from pins_history WHERE pinId = ? ORDER BY id DESC LIMIT 1")
    @UseRowMapper(PinValueMapper.class)
    Object getLastValueById(final int id);

    /**
     * Inserts the {@link PinModel}
     *
     * @param model
     * @return auto-generated key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO pins (connectorId, uid, index) VALUES (:connectorId, :uid, :index)")
    int insert(@BindBean final PinModel model);

    /**
     * Deletes the {@link PinModel} by primary key
     *
     * @param id the id of pin to be deleted
     */
    @SqlUpdate("DELETE FROM pins WHERE id = ?")
    void delete(final int id);

    /**
     * Updates the Index of {@link PinModel} by primary key
     *
     * @param id       the id of pin to be updated
     * @param newIndex the new index value
     */
    @SqlUpdate("UPDATE pins SET index = :newIndex WHERE id = :id")
    void updateIndex(final @Bind("id") int id, final @Bind("newIndex") int newIndex);

    /**
     * Inserts the value for {@link PinModel}
     *
     * @param id        the id of pin
     * @param value     the value to be inserted
     * @param valueType the class of value to be inserted
     * @return auto-generated key from {@code pins_history} table
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO pins_history (pinId, value, valueType) VALUES (?, ?, ?)")
    int insertValue(final int id, final String value, final Class<?> valueType);

    /**
     * Inserts the value for {@link PinModel}
     *
     * @param id    the id of pin
     * @param value the value to be inserted; may be null
     * @return auto-generated key from {@code pins_history} table
     */
    default int insertValue(final int id, final Object value) {
        final var valueAsString = value == null ? null : value.toString();
        final var valueType = value == null ? null : value.getClass();
        return insertValue(id, valueAsString, valueType);
    }
}
