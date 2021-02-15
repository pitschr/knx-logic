package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link DynamicPin}
 */
public class DynamicPinTest {

    @Test
    @DisplayName("Test get/set value of DynamicPin")
    public void testPin() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockDynamicConnector(testComponent);

        final var dynamicPin_0 = createNewDynamicPin(connectorMock, 0);
        final var dynamicPin_1 = createNewDynamicPin(connectorMock, 1);
        final var dynamicPin_2 = createNewDynamicPin(connectorMock, 2);

        // getValue(..)
        assertThat(testComponent.inputFields).containsExactly(13, 17, 19);
        assertThat(dynamicPin_0.getValue()).isEqualTo(13);
        assertThat(dynamicPin_1.getValue()).isEqualTo(17);
        assertThat(dynamicPin_2.getValue()).isEqualTo(19);

        // setValue()
        dynamicPin_0.setValue(23);
        dynamicPin_1.setValue(29);
        dynamicPin_2.setValue(31);

        // getValue() #2
        assertThat(testComponent.inputFields).containsExactly(23, 29, 31);
        assertThat(dynamicPin_0.getValue()).isEqualTo(23);
        assertThat(dynamicPin_1.getValue()).isEqualTo(29);
        assertThat(dynamicPin_2.getValue()).isEqualTo(31);

        // toString()
        assertThat(dynamicPin_0).hasToString(
                String.format("DynamicPin{uid=%s, fieldName=inputFields, fieldClassName=java.lang.Integer, index=0, value=23, refresh=true}", dynamicPin_0.getUid())
        );
        assertThat(dynamicPin_1).hasToString(
                String.format("DynamicPin{uid=%s, fieldName=inputFields, fieldClassName=java.lang.Integer, index=1, value=29, refresh=true}", dynamicPin_1.getUid())
        );
        assertThat(dynamicPin_2).hasToString(
                String.format("DynamicPin{uid=%s, fieldName=inputFields, fieldClassName=java.lang.Integer, index=2, value=31, refresh=true}", dynamicPin_2.getUid())
        );
    }

    @Test
    @DisplayName("Test refresh flag of DynamicPin")
    public void testRefreshFlag() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockDynamicConnector(testComponent);

        final var dynamicPin = createNewDynamicPin(connectorMock, 1);
        assertThat(dynamicPin.isRefresh()).isTrue(); // because of init
        assertThat(dynamicPin.refreshCount()).isEqualTo(1);
        dynamicPin.clearRefresh();

        // 17 -> 4711
        dynamicPin.setValue(4711);
        assertThat(dynamicPin.isRefresh()).isTrue();
        assertThat(dynamicPin.refreshCount()).isEqualTo(2);
        dynamicPin.clearRefresh();

        // remain same value (no refresh)
        dynamicPin.setValue(4711);
        assertThat(dynamicPin.isRefresh()).isFalse();
        assertThat(dynamicPin.refreshCount()).isEqualTo(2);
        dynamicPin.clearRefresh();

        // 4711 -> 37
        dynamicPin.setValue(37);
        assertThat(dynamicPin.isRefresh()).isTrue();
        assertThat(dynamicPin.refreshCount()).isEqualTo(3);
        dynamicPin.clearRefresh();

        // toString()
        assertThat(dynamicPin).hasToString(
                String.format("DynamicPin{uid=%s, fieldName=inputFields, fieldClassName=java.lang.Integer, index=1, value=37, refresh=false}", dynamicPin.getUid())
        );
    }

    private DynamicPin createNewDynamicPin(final DynamicConnector connector,
                                           final int index) {
        final var dynamicPin = new DynamicPin(connector, index);
        assertThat(dynamicPin.getUid()).isNotNull();
        assertThat(dynamicPin.getConnector()).isSameAs(connector);
        assertThat(dynamicPin.getDescriptor()).isInstanceOf(OutputDescriptor.class);
        return dynamicPin;
    }

    private DynamicConnector mockDynamicConnector(final Object owner) {
        final Field field;
        try {
            field = owner.getClass().getDeclaredField("inputFields");
        } catch (final Exception e) {
            throw new AssertionError(e);
        }

        final var descriptorMock = mock(OutputDescriptor.class);
        when(descriptorMock.getOwner()).thenReturn(owner);
        when(descriptorMock.getField()).thenReturn(field);
        when(descriptorMock.getName()).thenReturn(field.getName());
        doReturn(Integer.class).when(descriptorMock).getFieldValueClass();

        final var connectorMock = mock(DynamicConnector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        return connectorMock;
    }

    /**
     * Internal test component for testing purpose only
     */
    private static class TestComponent implements Logic {
        // we instantiate the list here manually, normally it will be
        // done by DynamicConnector, but here we want to test the DynamicPin
        @Input
        private List<Integer> inputFields = Arrays.asList(13, 17, 19);

        @Override
        public void logic() {
            // NO-OP
        }
    }
}
