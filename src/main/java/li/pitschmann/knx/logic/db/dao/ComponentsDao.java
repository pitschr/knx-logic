package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/**
 * DAO for components
 *
 * @author PITSCHR
 */
public interface ComponentsDao {
    /**
     * Returns the total size for all components
     *
     * @return
     */
    @SqlQuery("SELECT COUNT(*) FROM components")
    int size();

    /**
     * Returns the component for given {@code id}
     *
     * @param id primary key of component
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
    ComponentModel getByUid(final UID uid);

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
     * @param id the identifier / primary key of component
     * @return if delete was sucecssful
     */
    @SqlUpdate("DELETE FROM components WHERE id = :id")
    void deleteById(final int id);
}
