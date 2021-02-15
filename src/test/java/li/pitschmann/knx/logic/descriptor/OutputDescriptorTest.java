package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.Trigger;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.components.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link OutputDescriptor}
 */
public class OutputDescriptorTest {

    @Test
    @DisplayName("Initial OutputDescriptor")
    public void testInitialDescriptor() {
        final var objectMock = mock(Object.class);
        final var fieldMock = mock(Field.class);
        doReturn(String.class).when(fieldMock).getType();

        final var outputMock = mock(Output.class);
        when(outputMock.min()).thenReturn(1);
        when(outputMock.max()).thenReturn(1);
        when(outputMock.trigger()).thenReturn(Trigger.ON_INIT_AND_CHANGE);

        final var descriptor = new OutputDescriptor(objectMock, outputMock, fieldMock);
        assertThat(descriptor.isAlwaysTrigger()).isFalse();
        assertThat(descriptor).hasToString( //
                String.format("OutputDescriptor{" + //
                                "owner=%s, " +  //
                                "name=null, " + //
                                "field=%s, " +  //
                                "fieldValueClass=%s, " + //
                                "min=1, " + //
                                "max=1, " + //
                                "alwaysTrigger=false}",
                        objectMock, fieldMock, String.class)
        );
    }

    @Test
    @DisplayName("OutputDescriptor based on Output annotation")
    public void testDescriptor() {
        final var componentMock = mock(Component.class);
        final var fieldMock = mock(Field.class);
        doReturn(Long.class).when(fieldMock).getType();

        final var outputMock = mock(Output.class);
        when(outputMock.min()).thenReturn(19);
        when(outputMock.max()).thenReturn(21);
        when(outputMock.trigger()).thenReturn(Trigger.ALWAYS);

        final var descriptor = new OutputDescriptor(componentMock, outputMock, fieldMock);
        assertThat(descriptor.isAlwaysTrigger()).isTrue();
        assertThat(descriptor).hasToString( //
                String.format("OutputDescriptor{" + //
                                "owner=%s, " +  //
                                "name=null, " + //
                                "field=%s, " +  //
                                "fieldValueClass=%s, " + //
                                "min=19, " + //
                                "max=21, " + //
                                "alwaysTrigger=true}",
                        componentMock, fieldMock, Long.class)
        );
    }

}
