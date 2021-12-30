package li.pitschmann.knx.logic.db.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link PinLinkModel}
 */
class PinLinkModelTest {

    @Test
    @DisplayName("Create PinLinkModel (pin1 < pin2)")
    void testPin1SmallerThanPin2() {
        final var model = PinLinkModel.create(13, 17);
        assertThat(model.getPin1()).isEqualTo(13);
        assertThat(model.getPin2()).isEqualTo(17);

        assertThat(model).hasToString("PinLinkModel{" + //
                "id=-1, " + // -1 because of not persisted
                "pin1=13, " + //
                "pin2=17" + //
                "}"
        );
    }

    @Test
    @DisplayName("Create PinLinkModel (pin1 > pin2)")
    void testPin1BiggerThanPin2() {
        final var model = PinLinkModel.create(31, 17);
        assertThat(model.getPin1()).isEqualTo(17); // pin1 has lower value
        assertThat(model.getPin2()).isEqualTo(31); // pin2 has higher value

        assertThat(model).hasToString("PinLinkModel{" + //
                "id=-1, " + // -1 because of not persisted
                "pin1=17, " + //
                "pin2=31" + //
                "}"
        );
    }

}
