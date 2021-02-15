package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.uid.StaticUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link ComponentModel}
 */
public class ComponentModelTest {

    @Test
    @DisplayName("Basic Test for ComponentModel")
    public void test() {
        final var uidMock = mock(StaticUID.class);
        when(uidMock.toString()).thenReturn("STATIC-UID");
        final var componentMock = mock(LogicComponent.class);
        when(componentMock.getUid()).thenReturn(uidMock);

        final var model = ComponentModel.builder()
                .uid(uidMock)
                .className(LogicA.class)
                .componentType(ComponentType.LOGIC)
                .build();

        assertThat(model.getUid()).isInstanceOf(StaticUID.class);
        assertThat(model.getClassName()).isSameAs(LogicA.class);
        assertThat(model.getComponentType()).isSameAs(ComponentType.LOGIC);
        assertThat(model).hasToString("" + //
                "ComponentModel{" + //
                "uid=STATIC-UID, " +
                "className=test.components.LogicA, " + //
                "componentType=LOGIC" + //
                "}"
        );
    }
}
