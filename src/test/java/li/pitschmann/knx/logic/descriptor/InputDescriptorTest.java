package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.Trigger;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.components.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link InputDescriptor}
 */
public class InputDescriptorTest {

    @Test
    @DisplayName("Initial InputDescriptor")
    public void testInitialDescriptor() {
        final var componentMock = mock(Component.class);
        final var fieldMock = mock(Field.class);
        doReturn(String.class).when(fieldMock).getType();

        final var inputMock = mock(Input.class);
        when(inputMock.min()).thenReturn(1);
        when(inputMock.max()).thenReturn(1);
        when(inputMock.trigger()).thenReturn(Trigger.ON_INIT_AND_CHANGE);

        final var descriptor = new InputDescriptor(componentMock, inputMock, fieldMock);
        assertThat(descriptor.getMin()).isOne();
        assertThat(descriptor.getMax()).isOne();
        assertThat(descriptor.isAlwaysTrigger()).isFalse();
        assertThat(descriptor).hasToString( //
                String.format("InputDescriptor{" + //
                                "owner=%s, " +  //
                                "name=null, " + //
                                "field=%s, " +  //
                                "fieldType=java.lang.String, " + //
                                "min=1, " +
                                "max=1, " +
                                "alwaysTrigger=false}",
                        componentMock, fieldMock)
        );
    }

    @Test
    @DisplayName("InputDescriptor based on Input annotation")
    public void testDescriptor() {
        final var componentMock = mock(Component.class);
        final var fieldMock = mock(Field.class);
        doReturn(Integer.class).when(fieldMock).getType();

        final var inputMock = mock(Input.class);
        when(inputMock.min()).thenReturn(13);
        when(inputMock.max()).thenReturn(17);
        when(inputMock.trigger()).thenReturn(Trigger.ALWAYS);

        final var descriptor = new InputDescriptor(componentMock, inputMock, fieldMock);
        assertThat(descriptor.getMin()).isEqualTo(13);
        assertThat(descriptor.getMax()).isEqualTo(17);
        assertThat(descriptor.isAlwaysTrigger()).isTrue();
        assertThat(descriptor).hasToString( //
                String.format("InputDescriptor{" + //
                                "owner=%s, " +  //
                                "name=null, " + //
                                "field=%s, " +  //
                                "fieldType=java.lang.Integer, " + //
                                "min=13, " +
                                "max=17, " +
                                "alwaysTrigger=true}",
                        componentMock, fieldMock)
        );
    }

}
