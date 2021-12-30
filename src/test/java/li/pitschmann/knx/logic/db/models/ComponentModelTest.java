package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ComponentModel}
 */
class ComponentModelTest {

    @Test
    @DisplayName("Conversion to ComponentModel")
    void testBuilder() {
        final var model = ComponentModel.builder()
                .uid(createUid("COMPONENT-UID"))
                .className(LogicA.class.getName())
                .componentType(ComponentType.LOGIC)
                .build();

        assertThat(model.getUid()).isEqualTo(createUid("COMPONENT-UID"));
        assertThat(model.getClassName()).isEqualTo("test.components.LogicA");
        assertThat(model.getComponentType()).isSameAs(ComponentType.LOGIC);
        assertThat(model).hasToString("" + //
                "ComponentModel{" + //
                "id=-1, " + // -1 because of not persisted
                "uid=COMPONENT-UID, " +
                "className=test.components.LogicA, " + //
                "componentType=LOGIC" + //
                "}"
        );
    }
}
