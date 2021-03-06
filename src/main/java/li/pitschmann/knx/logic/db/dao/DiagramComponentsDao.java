package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.DiagramComponentModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'diagram_components' table
 *
 * @author PITSCHR
 */
public interface DiagramComponentsDao {

    /**
     * Returns all diagram component models by diagramId
     *
     * @return list of {@link DiagramComponentModel}
     */
    @SqlQuery("SELECT * FROM diagram_components WHERE diagramId = ?")
    List<DiagramComponentModel> byDiagramId(final int diagramId);

    /**
     * Inserts a new {@link DiagramComponentModel} into database
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO diagram_components (diagramId, componentId, positionX, positionY) " +
            "VALUES (:diagramId, :componentId, :positionX, :positionY)")
    int insert(@BindBean final DiagramComponentModel model);

    /**
     * Updates an existing {@link DiagramComponentModel} in database
     *
     * @param model model to be updated
     * @return primary key of updated model
     */
    @SqlUpdate("UPDATE diagram_components SET diagramId = :diagramId, componentId = :componentId, " +
            "positionX = :positionX, positionY = :positionY WHERE id = :id")
    int update(@BindBean final DiagramComponentModel model);

    /**
     * Deletes an existing {@link DiagramComponentModel} from database
     *
     * @param id the model identifier
     */
    @SqlUpdate("DELETE FROM diagram_components WHERE id = ?")
    void delete(final int id);
}
