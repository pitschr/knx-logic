package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.uid.StaticUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link PinModel}
 */
public class PinModelTest {

    @Test
    @DisplayName("Test conversion from Pin to a PinModel")
    public void test() {
        final var uidMock = mock(StaticUID.class);
        when(uidMock.toString()).thenReturn("PIN-UID");

        final var model = PinModel.builder() //
                .uid(uidMock) //
                .connectorId(21) //
                .index(23) //
                .build();

        assertThat(model.getUid()).isInstanceOf(StaticUID.class);
        assertThat(model.getConnectorId()).isEqualTo(21);
        assertThat(model.getIndex()).isEqualTo(23);

        assertThat(model).hasToString("PinModel{" + //
                "id=-1, " + // -1 because of not persisted
                "connectorId=21, " + //
                "uid=PIN-UID, " + //
                "index=23" + //
                "}"
        );
    }
}
