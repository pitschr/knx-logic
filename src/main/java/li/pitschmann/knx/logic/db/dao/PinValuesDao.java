package li.pitschmann.knx.logic.db.dao;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.logic.db.jdbi.mappers.row.PinValueMapper;
import li.pitschmann.knx.logic.db.models.PinModel;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.statement.UseRowMapper;

/**
 * DAO for 'pins' table
 *
 * @author PITSCHR
 */
public interface PinValuesDao {

    /**
     * Returns the last value by pin id from {@code pin_values} table
     *
     * @param pinId the identifier of pin model
     * @return the last value
     */
    @SqlQuery("SELECT value, valueType FROM pin_values " +
            // H2 selects pinId per default as it is an index as well (due reference to 'pins' table)
            // and omitting the special index 'pin_values_desc_index' that is created for this purpose
            "USE INDEX (pin_values_desc_index) " +
            "WHERE pinId = ? ORDER BY pinId, id DESC LIMIT 1")
    @UseRowMapper(PinValueMapper.class)
    Object lastValueByPinId(final int pinId);

    /**
     * Inserts the value for {@link PinModel}
     *
     * @param id        the identifier of pin model
     * @param value     the value to be inserted; may be null
     * @param valueType the class of value to be inserted; may be null if value is null too
     * @return auto-generated key from {@code pin_values} table
     */
    @GetGeneratedKeys
    @SqlUpdate("INSERT INTO pin_values (pinId, value, valueType) VALUES (?, ?, ?)")
    int insert(final int id, final @Nullable String value, final @Nullable Class<?> valueType);

    /**
     * Inserts the value for {@link PinModel}
     *
     * @param id    the identifier of pin model
     * @param value the value to be inserted; may be null
     * @return auto-generated key from {@code pin_values} table
     */
    default int insert(final int id, final @Nullable Object value) {
        final var valueAsString = value == null ? null : value.toString();
        final var valueType = value == null ? null : value.getClass();
        return insert(id, valueAsString, valueType);
    }
}
