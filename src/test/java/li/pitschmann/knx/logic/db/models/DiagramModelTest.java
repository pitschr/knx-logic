package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.uid.StaticUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link DiagramModel}
 */
class DiagramModelTest {

    @Test
    @DisplayName("Conversion to a DiagramModel")
    void testBuilder() {
        final var model = DiagramModel.builder() //
                .uid(createUid("DIAGRAM-UID")) //
                .name("DIAGRAM-NAME") //
                .description("DIAGRAM-DESCRIPTION") //
                .build();

        assertThat(model.getUid()).isInstanceOf(StaticUID.class);
        assertThat(model.getName()).isEqualTo("DIAGRAM-NAME");
        assertThat(model.getDescription()).isEqualTo("DIAGRAM-DESCRIPTION");

        assertThat(model).hasToString("DiagramModel{" + //
                "id=-1, " + // -1 because of not persisted
                "uid=DIAGRAM-UID, " + //
                "name=DIAGRAM-NAME, " + //
                "description=DIAGRAM-DESCRIPTION" + //
                "}"
        );
    }
}
