package li.pitschmann.knx.logic;

import li.pitschmann.knx.logic.uid.UID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link WorkflowEntry}
 */
class WorkflowEntryTest {

    @Test
    @DisplayName("WorkflowEntry with 'FOOBAR' as string value")
    void testWorkflowEntry() {
        final var mockUid = mock(UID.class);
        when(mockUid.toString()).thenReturn("WORKFLOW-UID-1");

        final var entry = new WorkflowEntry(mockUid, "FOOBAR");

        // test methods
        assertThat(entry.getInstant()).isNotNull();
        assertThat(entry.getUid()).isNotNull().hasToString("WORKFLOW-UID-1");
        assertThat(entry.getValue()).isInstanceOf(String.class).isEqualTo("FOOBAR");

        // test toString()
        assertThat(entry).hasToString(
                String.format("WorkflowEntry{instant=%s, uid=WORKFLOW-UID-1, value=FOOBAR}", entry.getInstant())
        );
    }

    @Test
    @DisplayName("WorkflowEntry with NULL value")
    void testWithNullValue() {
        final var mockUid = mock(UID.class);
        when(mockUid.toString()).thenReturn("WORKFLOW-UID-2");

        final var entry = new WorkflowEntry(mockUid, null);

        // test methods
        assertThat(entry.getInstant()).isNotNull();
        assertThat(entry.getUid()).isNotNull().hasToString("WORKFLOW-UID-2");
        assertThat(entry.getValue()).isNull();

        // test toString()
        assertThat(entry).hasToString(
                String.format("WorkflowEntry{instant=%s, uid=WORKFLOW-UID-2, value=null}", entry.getInstant())
        );
    }

    @Test
    @DisplayName("WorkflowEntry with byte array as value")
    void testWithByteArray() {
        final var mockUid = mock(UID.class);
        when(mockUid.toString()).thenReturn("WORKFLOW-UID-3");

        final var entry = new WorkflowEntry(mockUid, new byte[]{0x01, 0x02, 0x03, 0x04});

        // test methods
        assertThat(entry.getInstant()).isNotNull();
        assertThat(entry.getUid()).isNotNull().hasToString("WORKFLOW-UID-3");

        final var value = entry.getValue();
        assertThat(value).isInstanceOf(byte[].class);
        assertThat((byte[]) value).containsExactly(0x01, 0x02, 0x03, 0x04);

        // test toString()
        assertThat(entry).hasToString(
                String.format("WorkflowEntry{instant=%s, uid=WORKFLOW-UID-3, value=0x01 02 03 04}", entry.getInstant())
        );
    }
}
