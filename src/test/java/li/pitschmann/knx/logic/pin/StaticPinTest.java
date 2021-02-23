package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.descriptor.InputDescriptor;
import li.pitschmann.knx.logic.helpers.ValueHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link StaticPin}
 */
public class StaticPinTest {

    @Test
    @DisplayName("Test uid, connector and descriptor of StaticPin")
    public void testStaticPin() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "objectField");

        final var staticPin = new StaticPin(connectorMock);
        assertThat(staticPin.getUid()).isNotNull();
        assertThat(staticPin.getConnector()).isSameAs(connectorMock);
        assertThat(staticPin.getDescriptor()).isInstanceOf(InputDescriptor.class);
    }

    @Test
    @DisplayName("Test get/set object value of StaticPin")
    public void testObjectValue() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "objectField");
        final var staticPin = new StaticPin(connectorMock);

        // getValue() / setValue(..)
        assertThat(testComponent.objectField).isNull();
        assertThat(staticPin.getValue()).isNull();

        staticPin.setValue("foo");
        assertThat(testComponent.objectField).isEqualTo("foo");
        assertThat(staticPin.getValue()).isEqualTo("foo");

        staticPin.setValue(null);
        assertThat(testComponent.objectField).isNull();
        assertThat(staticPin.getValue()).isNull();
    }

    @Test
    @DisplayName("Test get/set string value of StaticPin")
    public void testStringValue() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "stringField");
        final var staticPin = new StaticPin(connectorMock);

        // getValue() / setValue(..)
        assertThat(testComponent.stringField).isEmpty();
        assertThat((String) staticPin.getValue()).isEmpty();

        staticPin.setValue("bar");
        assertThat(testComponent.stringField).isEqualTo("bar");
        assertThat(staticPin.getValue()).isEqualTo("bar");

        staticPin.setValue(null);
        assertThat(testComponent.stringField).isEmpty();
        assertThat((String) staticPin.getValue()).isEmpty();
    }

    @Test
    @DisplayName("Test get/set primitive value of StaticPin")
    public void testPrimitiveValue() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "primitiveField");
        final var staticPin = new StaticPin(connectorMock);

        // getValue() / setValue(..)
        assertThat(testComponent.primitiveField).isZero();
        assertThat(staticPin.getValue()).isEqualTo(0);

        staticPin.setValue(4711);
        assertThat(testComponent.primitiveField).isEqualTo(4711);
        assertThat(staticPin.getValue()).isEqualTo(4711);

        staticPin.setValue(null);
        assertThat(testComponent.primitiveField).isEqualTo(0);
        assertThat(staticPin.getValue()).isEqualTo(0);
    }

    @Test
    @DisplayName("Test toString() of StaticPin")
    public void testToString() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "objectField");
        final var staticPin = new StaticPin(connectorMock);

        // toString()
        assertThat(staticPin).hasToString(
                String.format("StaticPin{uid=%s, fieldName=objectField, fieldType=java.lang.Object, value=null, refresh=true}", staticPin.getUid())
        );
    }

    @Test
    @DisplayName("Test refresh flag of StaticPin")
    public void testRefreshFlag() {
        final var testComponent = new TestComponent();
        final var connectorMock = mockStaticConnector(testComponent, "objectField");

        final var staticPin = new StaticPin(connectorMock);
        assertThat(staticPin.isRefresh()).isTrue(); // because of init
        assertThat(staticPin.refreshCount()).isEqualTo(1);
        staticPin.clearRefresh();

        // null -> "foo"
        staticPin.setValue("foo");
        assertThat(staticPin.isRefresh()).isTrue();
        assertThat(staticPin.refreshCount()).isEqualTo(2);
        staticPin.clearRefresh();

        // remain same value (no refresh)
        staticPin.setValue("foo");
        assertThat(staticPin.isRefresh()).isFalse();
        assertThat(staticPin.refreshCount()).isEqualTo(2);
        staticPin.clearRefresh();

        // "foo" -> "bar"
        staticPin.setValue("bar");
        assertThat(staticPin.isRefresh()).isTrue();
        assertThat(staticPin.refreshCount()).isEqualTo(3);
        staticPin.clearRefresh();

        // toString()
        assertThat(staticPin).hasToString(
                String.format("StaticPin{uid=%s, fieldName=objectField, fieldType=java.lang.Object, value=bar, refresh=false}", staticPin.getUid())
        );
    }

    private StaticConnector mockStaticConnector(final Object owner, final String fieldName) {
        final Field field;
        try {
            field = owner.getClass().getDeclaredField(fieldName);
        } catch (final Exception e) {
            throw new AssertionError(e);
        }

        final var descriptorMock = mock(InputDescriptor.class);
        when(descriptorMock.getOwner()).thenReturn(owner);
        when(descriptorMock.getField()).thenReturn(field);
        when(descriptorMock.getName()).thenReturn(field.getName());
        doReturn(ValueHelper.getFieldType(field)).when(descriptorMock).getFieldType();

        final var connectorMock = mock(StaticConnector.class);
        when(connectorMock.getDescriptor()).thenReturn(descriptorMock);
        return connectorMock;
    }

    /**
     * Internal test component for testing purpose only
     */
    private static class TestComponent implements Logic {
        @Input
        private Object objectField;

        @Input
        private String stringField;

        @Input
        private int primitiveField;

        @Override
        public void logic() {
            // NO-OP
        }
    }
}
