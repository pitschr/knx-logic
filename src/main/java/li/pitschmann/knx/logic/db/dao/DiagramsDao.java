package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.DiagramModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'diagrams' table
 *
 * @author PITSCHR
 */
public interface DiagramsDao {
    /**
     * Returns the total size for all diagrams
     *
     * @return the total size of diagrams
     */
    @SqlQuery("SELECT COUNT(*) FROM diagrams")
    int size();

    /**
     * Returns all diagrams
     *
     * @return list of {@link DiagramModel}
     */
    @SqlQuery("SELECT * FROM diagrams")
    List<DiagramModel> all();

    /**
     * Returns the diagram for given {@code id}
     *
     * @param id primary key of diagram
     * @return {@link DiagramModel}
     */
    @SqlQuery("SELECT * FROM diagrams WHERE id = ?")
    DiagramModel find(final int id);

    /**
     * Returns the diagram for given {@code uid}
     *
     * @param uid the {@link UID} of diagram
     * @return {@link DiagramModel}
     */
    @SqlQuery("SELECT * FROM diagrams WHERE uid = ?")
    DiagramModel find(final UID uid);

    /**
     * Inserts a new {@link DiagramModel} into database
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO diagrams (name, description) VALUES (:name, :description)")
    int insert(@BindBean final DiagramModel model);

    /**
     * Updates an existing {@link DiagramModel} in database
     *
     * @param model model to be updated
     * @return primary key of updated model
     */
    @SqlUpdate("UPDATE diagrams SET name = :name, description = :description WHERE id = :id")
    int update(@BindBean final DiagramModel model);

    /**
     * Deletes an existing {@link DiagramModel} from database
     *
     * @param uid UID of model to be deleted
     */
    @SqlUpdate("DELETE FROM diagrams WHERE uid = ?")
    void delete(final UID uid);
}
