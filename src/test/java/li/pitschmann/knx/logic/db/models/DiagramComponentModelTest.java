package li.pitschmann.knx.logic.db.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link DiagramComponentModel}
 */
class DiagramComponentModelTest {

    @Test
    @DisplayName("Conversion to DiagramComponentModel")
    void testBuilder() {
        final var model = DiagramComponentModel.builder() //
                .diagramId(4711) //
                .componentId(7411) //
                .position(13, 17) //
                .build();

        assertThat(model.getDiagramId()).isEqualTo(4711);
        assertThat(model.getComponentId()).isEqualTo(7411);
        assertThat(model.getPositionX()).isEqualTo(13);
        assertThat(model.getPositionY()).isEqualTo(17);

        assertThat(model).hasToString("DiagramComponentModel{" + //
                "id=-1, " + // -1 because of not persisted
                "diagramId=4711, " + //
                "componentId=7411, " + //
                "positionX=13, " + //
                "positionY=17" + //
                "}"
        );
    }

}
