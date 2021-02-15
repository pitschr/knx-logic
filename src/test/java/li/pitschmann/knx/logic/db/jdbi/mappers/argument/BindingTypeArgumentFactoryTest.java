package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link BindingTypeArgumentFactory}
 */
public class BindingTypeArgumentFactoryTest {

    @Test
    @DisplayName("Test with BindingType.STATIC")
    public void testWithStatic() throws SQLException {
        final var factory = new BindingTypeArgumentFactory();
        final var argument = factory.build(BindingType.class, BindingType.STATIC, null).get();

        final var statementMock = mock(PreparedStatement.class);
        argument.apply(0, statementMock, null);

        verify(statementMock).setInt(anyInt(), eq(0)); // STATIC.getIndex()
    }

    @Test
    @DisplayName("Test with BindingType.DYNAMIC")
    public void testWithDynamic() throws SQLException {
        final var factory = new BindingTypeArgumentFactory();
        final var argument = factory.build(BindingType.class, BindingType.DYNAMIC, null).get();

        final var statementMock = mock(PreparedStatement.class);
        argument.apply(0, statementMock, null);

        verify(statementMock).setInt(anyInt(), eq(1)); // DYNAMIC.getIndex()
    }
}
