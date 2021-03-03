package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.logic.db.models.PinLinkModel;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

/**
 * DAO for 'pin_links' table
 *
 * @author PITSCHR
 */
public interface PinLinksDao {

    /**
     * Returns the {@link PinLinkModel} for given {@code id}
     *
     * @param id the identifier of pin links model
     * @return {@link PinLinkModel}
     */
    @SqlQuery("SELECT * FROM pin_links WHERE id = ?")
    PinLinkModel getById(final int id);

    /**
     * Returns the component for given {@code uid}
     *
     * @param pinId the identifier of pin model
     * @return {@link PinLinkModel}
     */
    @SqlQuery("SELECT * FROM pin_links WHERE pin1 = :pinId OR pin2 = :pinId")
    PinLinkModel getByPinId(final int pinId);

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
     * Deletes the {@link PinLinkModel} by primary key
     *
     * @param id the identifier of pin links model
     */
    @SqlUpdate("DELETE FROM pins WHERE id = ?")
    void delete(final int id);
}
