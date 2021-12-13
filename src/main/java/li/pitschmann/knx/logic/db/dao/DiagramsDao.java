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
public interface DiagramsDao extends GenericDao<DiagramModel> {
    @Override
    @SqlQuery("SELECT COUNT(*) FROM diagrams")
    int size();

    @Override
    @SqlQuery("SELECT * FROM diagrams")
    List<DiagramModel> all();

    @Override
    @SqlQuery("SELECT * FROM diagrams WHERE uid = ?")
    DiagramModel find(final UID uid);

    @Override
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO diagrams (name, description) VALUES (:name, :description)")
    int insert(@BindBean final DiagramModel model);

    @Override
    @SqlUpdate("UPDATE diagrams SET name = :name, description = :description WHERE uid = :uid")
    void update(@BindBean final DiagramModel model);

    @Override
    @SqlUpdate("DELETE FROM diagrams WHERE uid = ?")
    void delete(final UID uid);
}
