package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'components' table
 *
 * @author PITSCHR
 */
public interface ComponentsDao {
    /**
     * Returns the total size for all components
     *
     * @return the total size of components
     */
    @SqlQuery("SELECT COUNT(*) FROM components")
    int size();

    /**
     * Returns all components
     *
     * @return list of {@link ComponentModel}
     */
    @SqlQuery("SELECT * FROM components")
    List<ComponentModel> all();

    /**
     * Returns the component for given {@code id}
     *
     * @param id the identifier of component
     * @return {@link ComponentModel}
     */
    @SqlQuery("SELECT * FROM components WHERE id = ?")
    ComponentModel getById(final int id);

    /**
     * Returns the component for given {@code uid}
     *
     * @param uid {@link UID} of component
     * @return {@link ComponentModel}
     */
    @SqlQuery("SELECT * FROM components WHERE uid = ?")
    ComponentModel getByUID(final UID uid);

    /**
     * Inserts a new {@link ComponentModel} into database
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO components (componentType, uid, className) VALUES (:componentType, :uid, :className)")
    int insert(@BindBean final ComponentModel model);

    /**
     * Updates an existing {@link ComponentModel} in database
     *
     * @param model model to be updated
     * @return primary key of component model that has been updated
     */
    @SqlUpdate("UPDATE components SET eventKey = :eventKey WHERE id = :id")
    int update(@BindBean final ComponentModel model);

    /**
     * Deletes an existing {@link ComponentModel} from database
     *
     * @param uid UID of model to be deleted
     */
    @SqlUpdate("DELETE FROM components WHERE uid = :uid")
    void delete(final UID uid);
}
