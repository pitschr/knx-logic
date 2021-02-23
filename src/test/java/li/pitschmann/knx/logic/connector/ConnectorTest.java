package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link Connector}
 */
public class ConnectorTest {

    @Test
    @DisplayName("Test for compatibility")
    public void testCompatibility() {
        final var descriptorMock = mock(FieldDescriptor.class);
        doReturn(Number.class).when(descriptorMock).getFieldType();

        final var connector = mock(Connector.class);
        when(connector.getDescriptor()).thenReturn(descriptorMock);
        when(connector.isCompatibleWith(any())).thenCallRealMethod();

        // Compatibility Matrix
        // ---------------------
        // Number number;
        // number = (Number)1;          // Compatible
        // number = Integer.valueOf(1); // Compatible
        // number = Long.valueOf(1L);   // Compatible
        // number = Double.valueOf(1d); // Compatible
        // number = Byte.valueOf("10"); // Compatible
        // number = new Object();       // Incompatible
        // number = "foo";              // Incompatible

        assertThat(connector.isCompatibleWith(Number.class)).isTrue();
        assertThat(connector.isCompatibleWith(Integer.class)).isTrue();
        assertThat(connector.isCompatibleWith(Long.class)).isTrue();
        assertThat(connector.isCompatibleWith(Double.class)).isTrue();
        assertThat(connector.isCompatibleWith(Byte.class)).isTrue();

        assertThat(connector.isCompatibleWith(Object.class)).isFalse();
        assertThat(connector.isCompatibleWith(String.class)).isFalse();
    }

    @Test
    @DisplayName("Test for #getName()")
    public void testGetName() {
        final var descriptorMock = mock(FieldDescriptor.class);
        when(descriptorMock.getOwner()).thenReturn(new ArrayList<>());
        when(descriptorMock.getName()).thenReturn("myFieldName");

        final var connector = mock(Connector.class);
        when(connector.getDescriptor()).thenReturn(descriptorMock);
        when(connector.getName()).thenCallRealMethod();

        assertThat(connector.getName()).isEqualTo("java.util.ArrayList#myFieldName");
    }
}
