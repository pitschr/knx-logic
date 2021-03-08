package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.jdbi.mappers.data.TwoTupleUID;
import li.pitschmann.knx.logic.db.jdbi.mappers.row.TwoTupleUIDMapper;
import li.pitschmann.knx.logic.db.models.PinLinkModel;
import li.pitschmann.knx.logic.uid.UID;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

import java.util.List;

/**
 * DAO for 'pin_links' table
 *
 * @author PITSCHR
 */
public interface PinLinksDao {
    /**
     * Returns the total size for all pin links
     *
     * @return size of all pin links
     */
    @SqlQuery("SELECT COUNT(*) FROM pin_links")
    int size();

    /**
     * Returns the {@link PinLinkModel} for given {@code id}
     *
     * @param id the identifier of pin links
     * @return {@link PinLinkModel}
     */
    @SqlQuery("SELECT * FROM pin_links WHERE id = ?")
    PinLinkModel find(final int id);

    /**
     * Returns list of {@link TwoTupleUID} for given diagram id
     *
     * @param diagramId the identifier of diagram
     * @return list of {@link TwoTupleUID}
     */
    @SqlQuery("SELECT p1.uid, p2.uid" +
            " FROM " +
            "        diagram_links dl " +
            "        INNER JOIN pin_links pl ON pl.id = dl.pinLinkId " +
            "        INNER JOIN pins p1 ON p1.id = pl.pin1 " +
            "        INNER JOIN pins p2 ON p2.id = pl.pin2 " +
            " WHERE " +
            "        dl.diagramId = ?")
    @UseRowMapper(TwoTupleUIDMapper.class)
    List<TwoTupleUID> getUidPairByDiagramId(final int diagramId);

    /**
     * Inserts the {@link PinLinkModel}
     *
     * @param model the pin model to be persisted
     * @return auto-generated key from {@code pin_links} table
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO pin_links (pin1, pin2) VALUES (:pin1, :pin2)")
    int insert(@BindBean final PinLinkModel model);

    /**
     * Deletes the {@link PinLinkModel} by the pin {@link UID}s
     *
     * @param uid of pin
     */
    @SqlUpdate("DELETE FROM pin_links pl WHERE id IN (" +
            "       SELECT " +
            "           pl.id " +
            "       FROM " +
            "           pin_links pl" +
            "           INNER JOIN pins p ON (p.id = pl.pin1 OR p.id = pl.pin2) " +
            "       WHERE " +
            "           p.uid = ? " +
            ")")
    void delete(final UID uid);

    /**
     * Deletes the {@link PinLinkModel} by the two {@link UID}s
     *
     * @param uid1 uid of pin 1
     * @param uid2 uid of pin 2
     */
    @SqlUpdate("DELETE FROM pin_links pl WHERE id = (" +
            "       SELECT " +
            "           pl.id " +
            "       FROM " +
            "           pin_links pl" +
            "           INNER JOIN pins p1 ON p1.id = pl.pin1 " +
            "           INNER JOIN pins p2 ON p2.id = pl.pin2 " +
            "       WHERE " +
            "           (p1.uid = :uid1 AND p2.uid = :uid2) " +
            "           OR (p1.uid = :uid2 OR p2.uid = :uid1)" +
            ")")
    void delete(final @Bind("uid1") UID uid1, final @Bind("uid2") UID uid2);
}
