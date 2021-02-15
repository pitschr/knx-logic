package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ClassColumnMapper}
 */
public class ClassColumnMapperTest {

    @Test
    @DisplayName("Column Mapping of Class")
    public void testValueOf() throws SQLException {
        final var mapper = new ClassColumnMapper();
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(anyInt())).thenReturn("java.lang.LoremIpsum");
        when(resultSetMock.getString(eq(1))).thenReturn("java.lang.String");

        assertThat(mapper.map(resultSetMock, 1, null)).isSameAs(String.class);
        assertThatThrownBy(() -> mapper.map(resultSetMock, 2, null))
                .isInstanceOf(SQLException.class)
                .hasMessage("Could not find class 'java.lang.LoremIpsum' to be converted from String.");
    }
}
