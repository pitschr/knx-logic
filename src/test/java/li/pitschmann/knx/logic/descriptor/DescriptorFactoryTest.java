package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Test for {@link DescriptorFactory}
 */
public class DescriptorFactoryTest {

    @Test
    @DisplayName("Create Input Descriptor")
    public void testInputDescriptor() throws Throwable {
        final var inputField = TestComponent.class.getDeclaredField("input");

        final var descriptor = DescriptorFactory.createFieldDescriptor(mock(TestComponent.class), inputField, Input.class);
        assertThat(descriptor).isInstanceOf(InputDescriptor.class);
    }

    @Test
    @DisplayName("Create Output Descriptor")
    public void testOutputDescriptor() throws Throwable {
        final var outputField = TestComponent.class.getDeclaredField("output");

        final var descriptor = DescriptorFactory.createFieldDescriptor(mock(TestComponent.class), outputField, Output.class);
        assertThat(descriptor).isInstanceOf(OutputDescriptor.class);
    }

    @Test
    @DisplayName("ERROR: Create Descriptor from an unsupported annotation")
    public void testUnknownAnnotation() {
        assertThatThrownBy(() -> DescriptorFactory.createFieldDescriptor(mock(TestComponent.class), mock(Field.class), Deprecated.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Constructor not instantiable")
    public void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = DescriptorFactory.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }


    /**
     * Test Object
     */
    private static class TestComponent implements Logic {
        @Input
        private Object input;

        @Output
        private Object output;

        @Override
        public void logic() {
            // NO-OP
        }
    }

}
