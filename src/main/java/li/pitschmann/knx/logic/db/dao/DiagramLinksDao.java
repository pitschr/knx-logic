package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.DiagramLinkModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

/**
 * DAO for 'diagram_links' table
 *
 * @author PITSCHR
 */
public interface DiagramLinksDao {

    /**
     * Returns all {@link DiagramLinkModel} by diagram id
     *
     * @return list of {@link DiagramLinkModel}
     */
    @SqlQuery("SELECT * FROM diagram_links WHERE diagramId = ?")
    List<DiagramLinkModel> byDiagramId(final int diagramId);

    /**
     * Inserts a new {@link DiagramLinkModel} into database
     *
     * @param model model to be inserted
     * @return newly generated primary key
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO diagram_links (diagramId, sourcePinId, targetPinId, svgPath) " +
            "VALUES (:diagramId, :sourcePinId, :targetPinId, :svgPath)")
    int insert(@BindBean final DiagramLinkModel model);

    /**
     * Updates an existing {@link DiagramLinkModel} in database
     *
     * @param model model to be updated
     * @return primary key of updated model
     */
    @SqlUpdate("UPDATE diagram_links SET diagramId = :diagramId, sourcePinId = :sourcePinId, " +
            "targetPinId = :targetPinId, svgPath = :svgPath WHERE id = :id")
    int update(@BindBean final DiagramLinkModel model);

    /**
     * Deletes an existing {@link DiagramLinkModel} from database
     *
     * @param id the model identifier
     */
    @SqlUpdate("DELETE FROM diagram_links WHERE id = ?")
    void delete(final int id);
}
