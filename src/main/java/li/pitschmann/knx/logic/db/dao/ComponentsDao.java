package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
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

    @SqlQuery("SELECT * FROM components WHERE id = ?")
    ComponentModel find(final int id);

    @Override
    @SqlQuery("SELECT * FROM components WHERE uid = ?")
    ComponentModel find(final UID uid);

    @SqlQuery("SELECT * FROM components WHERE id IN (<ids>)")
    List<ComponentModel> find(final @BindList("ids") int[] ids);

    @Override
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO components (componentType, uid, className) VALUES (:componentType, :uid, :className)")
    int insert(@BindBean final ComponentModel model);

    @Override
    default void update(final int id, final ComponentModel model) {
        throw new UnsupportedOperationException("No update supported for ComponentModel");
    }

    @Override
    @SqlUpdate("DELETE FROM components WHERE uid = ?")
    void delete(final UID uid);

    /**
     * Returns all {@link ComponentModel} for given diagram {@link UID}
     *
     * @param uid the unique identifier of diagram
     * @return list of {@link ComponentModel}
     */
    @SqlQuery("SELECT " +
            "    c.* " +
            "  FROM diagram_components dc " +
            "    INNER JOIN diagrams d ON dc.diagramId = d.id " +
            "    INNER JOIN components c ON dc.componentId = c.id " +
            "  WHERE d.uid = ?")
    List<ComponentModel> byDiagramUid(final UID uid);
}
