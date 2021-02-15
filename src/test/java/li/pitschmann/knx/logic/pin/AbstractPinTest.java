package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.descriptor.InputDescriptor;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
import li.pitschmann.knx.logic.uid.UID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link AbstractPin}
 */
class AbstractPinTest {

    @SuppressWarnings({"rawtypes"})
    private static Connector createConnectorMock(final Class<? extends FieldDescriptor> fieldDescriptorClass) {
        final var descriptorMock = mock(fieldDescriptorClass);
        when(descriptorMock.getOwner()).thenReturn(mock(Logic.class));
        when(descriptorMock.getName()).thenReturn("dummyField");

        final var connectorMock = mock(Connector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        return connectorMock;
    }

    @Test
    @DisplayName("Test instantiation of AbstractPin with Field Descriptor")
    void testPin() {
        final var connectorMock = createConnectorMock(FieldDescriptor.class);

        final var pin = new TestPin(connectorMock);
        assertThat(pin.getConnector()).isSameAs(connectorMock);
        assertThat(pin.getDescriptor()).isInstanceOf(FieldDescriptor.class).isSameAs(connectorMock.getDescriptor());
        assertThat(pin.getUid()).isNotNull();
    }

    @Test
    @DisplayName("Test instantiation of AbstractPin with Input Descriptor")
    void testPinWithInputDescriptor() {
        final var connectorMock = createConnectorMock(InputDescriptor.class);

        final var pin = new TestPin(connectorMock);
        assertThat(pin.getConnector()).isSameAs(connectorMock);
        assertThat(pin.getDescriptor()).isInstanceOf(InputDescriptor.class).isSameAs(connectorMock.getDescriptor());
        assertThat(pin.getUid()).isNotNull();
        assertThat(pin.isAlwaysTrigger()).isFalse();
    }

    @Test
    @DisplayName("Test instantiation of AbstractPin with Input Descriptor and always='true' trigger")
    void testPinWithInputDescriptorAlwaysTrigger() {
        final var connectorMock = createConnectorMock(InputDescriptor.class);
        final var descriptorMock = (InputDescriptor) connectorMock.getDescriptor();
        when(descriptorMock.isAlwaysTrigger()).thenReturn(true);

        final var pin = new TestPin(connectorMock);
        assertThat(pin.getConnector()).isSameAs(connectorMock);
        assertThat(pin.getDescriptor()).isInstanceOf(InputDescriptor.class).isSameAs(connectorMock.getDescriptor());
        assertThat(pin.getUid()).isNotNull();
        assertThat(pin.isAlwaysTrigger()).isTrue();
    }

    @Test
    @DisplayName("Test instantiation of AbstractPin with Output Descriptor")
    void testPinWithOutputDescriptor() {
        final var connectorMock = createConnectorMock(OutputDescriptor.class);

        final var pin = new TestPin(connectorMock);
        assertThat(pin.getConnector()).isSameAs(connectorMock);
        assertThat(pin.getDescriptor()).isInstanceOf(OutputDescriptor.class).isSameAs(connectorMock.getDescriptor());
        assertThat(pin.getUid()).isNotNull();
    }

    @Test
    @DisplayName("Test refresh/clearRefresh behavior of AbstractPin")
    void testPinWithRefreshFlag() {
        final var pin = new TestPin(createConnectorMock(FieldDescriptor.class));

        // init
        assertThat(pin.isRefresh()).isTrue();
        assertThat(pin.refreshCount()).isEqualTo(1);

        // clear refresh
        pin.clearRefresh();
        assertThat(pin.isRefresh()).isFalse();
        assertThat(pin.refreshCount()).isEqualTo(1);

        // set refresh
        pin.setRefresh();
        assertThat(pin.isRefresh()).isTrue();
        assertThat(pin.refreshCount()).isEqualTo(2);

        // re-set refresh flag (without clearing before)
        pin.setRefresh();
        assertThat(pin.isRefresh()).isTrue();
        assertThat(pin.refreshCount()).isEqualTo(2); // remain same
    }

    @Test
    @DisplayName("Test the override of UID")
    void testPinUidOverride() {
        final var pin = new TestPin(createConnectorMock(FieldDescriptor.class));
        final var uidMock = mock(UID.class);

        // set UID
        pin.setUid(uidMock);

        // verify
        assertThat(pin.getUid()).isSameAs(uidMock);
    }

    /**
     * Implementation of AbstractPin for testing purposes
     */
    private static class TestPin extends AbstractPin<Connector> {

        TestPin(final Connector connector) {
            super(connector);
        }

        @Override
        public Object getValue() {
            return null;
        }

        @Override
        public void setValue(Object value) {
            // NO-OP
        }
    }
}
