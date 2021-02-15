package li.pitschmann.knx.logic.db.jdbi.mappers.column;

import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link EventKeyColumnMapper}
 */
public class EventKeyColumnMapperTest {

    @Test
    @DisplayName("Column Mapping of EventKey")
    public void testVariableEventKey() throws SQLException {
        final var mapper = new EventKeyColumnMapper();
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(eq(10))).thenReturn("CHANNEL:NAME");

        final var eventKey = mapper.map(resultSetMock, 10, null);
        assertThat(eventKey)
                .isEqualTo(new EventKey("CHANNEL", "NAME"));
    }
}
