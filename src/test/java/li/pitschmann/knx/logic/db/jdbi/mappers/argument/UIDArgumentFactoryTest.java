package li.pitschmann.knx.logic.db.jdbi.mappers.argument;

import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for {@link UIDArgumentFactory}
 */
public class UIDArgumentFactoryTest {

    @Test
    @DisplayName("Test UIDArgumentFactory with StaticUID")
    public void testWithStaticUID() throws SQLException {
        final var factory = new UIDArgumentFactory();
        final var argument = factory.build(UID.class, UIDFactory.createUid("STATIC-UID"), null).orElseThrow();

        final var statementMock = mock(PreparedStatement.class);
        argument.apply(0, statementMock, null);

        verify(statementMock).setString(anyInt(), eq("STATIC-UID"));
    }
}
