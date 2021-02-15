package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
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
 * Test for {@link BindingTypeColumnMapper}
 */
public class BindingTypeColumnMapperTest {

    @Test
    @DisplayName("Column Mapping of BindingType")
    public void testValueOf() throws SQLException {
        final var mapper = new BindingTypeColumnMapper();
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getInt(anyInt())).thenReturn(4711);
        when(resultSetMock.getInt(eq(10))).thenReturn(0);
        when(resultSetMock.getInt(eq(20))).thenReturn(1);

        assertThat(mapper.map(resultSetMock, 10, null)).isSameAs(BindingType.STATIC);
        assertThat(mapper.map(resultSetMock, 20, null)).isSameAs(BindingType.DYNAMIC);
        assertThatThrownBy(() -> mapper.map(resultSetMock, 30, null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
