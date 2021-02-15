package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.event.EventKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link EventKeyArgumentFactory}
 */
public class EventKeyArgumentFactoryTest {

    @Test
    @DisplayName("Test of EventKey persistence of EventKey")
    public void testVariableEventKey() throws SQLException {
        final var factory = new EventKeyArgumentFactory();
        final var argument = factory.build(EventKey.class, new EventKey("CHANNEL", "NAME"), null).get();

        final var statementMock = mock(PreparedStatement.class);
        argument.apply(0, statementMock, null);

        verify(statementMock).setString(anyInt(), eq("CHANNEL:NAME"));
    }
}
