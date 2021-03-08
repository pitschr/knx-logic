package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.uid.StaticUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link PinModel}
 */
class PinModelTest {

    @Test
    @DisplayName("Test conversion from Pin to a PinModel")
    void test() {
        final var model = PinModel.builder() //
                .uid(createUid("PIN-UID")) //
                .connectorId(21) //
                .index(23) //
                .build();

        assertThat(model.getUid()).isInstanceOf(StaticUID.class);
        assertThat(model.getConnectorId()).isEqualTo(21);
        assertThat(model.getIndex()).isEqualTo(23);

        assertThat(model).hasToString("PinModel{" + //
                "id=-1, " + // -1 because of not persisted
                "uid=PIN-UID, " + //
                "connectorId=21, " + //
                "index=23" + //
                "}"
        );
    }
}
