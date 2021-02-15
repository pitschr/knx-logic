package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link ClassArgumentFactory}
 */
public class ClassArgumentFactoryTest {

    @Test
    @DisplayName("Test ClassArgumentFactory with Integer class")
    public void testBuildWithIntegerClass() throws SQLException {
        final var factory = new ClassArgumentFactory();
        final var argument = factory.build(Integer.class, null);

        final var statementMock = mock(PreparedStatement.class);
        argument.apply(0, statementMock, null);

        verify(statementMock).setString(eq(0), eq("java.lang.Integer"));
    }

}
