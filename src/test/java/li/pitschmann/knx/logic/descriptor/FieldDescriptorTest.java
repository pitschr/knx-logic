package li.pitschmann.knx.logic.descriptor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link FieldDescriptor}
 */
public class FieldDescriptorTest {

    @Test
    @DisplayName("Test the Field Descriptor")
    public void testDescriptor() {
        final var objectMock = mock(Object.class);
        final var fieldMock = mock(Field.class);
        when(fieldMock.getName()).thenReturn("FIELD-NAME");
        doReturn(String.class).when(fieldMock).getType();

        final var descriptor = new FieldDescriptor(objectMock, fieldMock);
        assertThat(descriptor.getOwner()).isSameAs(objectMock);
        assertThat(descriptor.getName()).isEqualTo("FIELD-NAME");
        assertThat(descriptor.getField()).isSameAs(fieldMock);
        assertThat(descriptor.getFieldValueClass()).isEqualTo(String.class);
        assertThat(descriptor).hasToString( //
                String.format("FieldDescriptor{" + //
                                "owner=%s, " +  //
                                "field=%s, " +  //
                                "fieldValueClass=%s}", //
                        objectMock, fieldMock, String.class) //
        );
    }

}
