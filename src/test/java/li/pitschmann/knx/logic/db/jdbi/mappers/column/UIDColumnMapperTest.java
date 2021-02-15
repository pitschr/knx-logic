package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.uid.StaticUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link UIDColumnMapper}
 */
public class UIDColumnMapperTest {

    @Test
    @DisplayName("Column Mapping of UID")
    public void testValueOf() throws SQLException {
        final var mapper = new UIDColumnMapper();
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(anyInt())).thenReturn("UID-STRING");

        assertThat(mapper.map(resultSetMock, 0, null))
                .isInstanceOf(StaticUID.class)
                .hasToString("UID-STRING");
    }
}
