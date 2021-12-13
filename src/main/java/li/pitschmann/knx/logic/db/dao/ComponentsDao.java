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
public interface ComponentsDao extends GenericDao<ComponentModel> {
    @Override
    @SqlQuery("SELECT COUNT(*) FROM components")
    int size();

    @Override
    @SqlQuery("SELECT * FROM components")
    List<ComponentModel> all();

    @Override
    @SqlQuery("SELECT * FROM components WHERE uid = ?")
    ComponentModel find(final UID uid);

    @Override
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO components (componentType, uid, className) VALUES (:componentType, :uid, :className)")
    int insert(@BindBean final ComponentModel model);

    @Override
    @SqlUpdate("UPDATE components SET eventKey = :eventKey WHERE id = :id")
    void update(@BindBean final ComponentModel model);

    @Override
    @SqlUpdate("DELETE FROM components WHERE uid = ?")
    void delete(final UID uid);

    /**
     * Returns the {@link ComponentModel} for given {@code id}
     *
     * @param id the identifier of component
     * @return {@link ComponentModel}
     */
    @SqlQuery("SELECT * FROM components WHERE id = ?")
    ComponentModel find(final int id);

    /**
     * Returns all {@link ComponentModel} for given diagram {@code id}
     *
     * @param id the identifier of diagram
     * @return list of {@link ComponentModel}
     */
    @SqlQuery("SELECT c.* FROM diagram_components dc INNER JOIN components c ON dc.componentId = c.id WHERE dc.diagramId = ?")
    List<ComponentModel> byDiagramId(final int id);
}
