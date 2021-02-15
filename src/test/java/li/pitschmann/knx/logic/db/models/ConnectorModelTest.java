package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link ConnectorModel}
 */
class ConnectorModelTest {

    @Test
    @DisplayName("Test conversion from StaticConnector to a ConnectorModel")
    void testStatic() {
        final var connectorMock = mock(StaticConnector.class);

        final var model = ConnectorModel.builder() //
                .componentId(7) //
                .bindingType(connectorMock) //
                .connectorName("STATIC-CONNECTOR") //
                .build();

        assertThat(model.getComponentId()).isEqualTo(7);
        assertThat(model.getBindingType()).isSameAs(BindingType.STATIC);
        assertThat(model.getConnectorName()).isEqualTo("STATIC-CONNECTOR");

        assertThat(model.isStatic()).isTrue();
        assertThat(model.isDynamic()).isFalse();

        assertThat(model).hasToString("ConnectorModel{" + //
                "id=-1, " + // -1 because of not persisted
                "componentId=7, " + //
                "bindingType=STATIC, " + //
                "connectorName=STATIC-CONNECTOR" + //
                "}"
        );
    }

    @Test
    @DisplayName("Test conversion from DynamicConnector to a ConnectorModel")
    void testDynamic() {
        final var connectorMock = mock(DynamicConnector.class);

        final var model = ConnectorModel.builder() //
                .componentId(17) //
                .bindingType(connectorMock) //
                .connectorName("DYNAMIC-CONNECTOR") //
                .build();

        assertThat(model.getComponentId()).isEqualTo(17);
        assertThat(model.getBindingType()).isSameAs(BindingType.DYNAMIC);
        assertThat(model.getConnectorName()).isEqualTo("DYNAMIC-CONNECTOR");

        assertThat(model.isStatic()).isFalse();
        assertThat(model.isDynamic()).isTrue();

        assertThat(model).hasToString("ConnectorModel{" + //
                "id=-1, " + // -1 because of not persisted
                "componentId=17, " + //
                "bindingType=DYNAMIC, " + //
                "connectorName=DYNAMIC-CONNECTOR" + //
                "}"
        );
    }

    @Test
    @DisplayName("ERROR: Conversion of an unsupported connector")
    void testFailure() {
        final var builder = ConnectorModel.builder();
        assertThatThrownBy(() -> builder.bindingType(null)) // throwing exception
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unsupported connector: null");
    }
}
