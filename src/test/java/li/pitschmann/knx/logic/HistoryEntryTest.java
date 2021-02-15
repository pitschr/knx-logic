package li.pitschmann.knx.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for {@link HistoryEntry}
 *
 * @author PITSCHR
 */
public final class HistoryEntryTest {

    @Test
    @DisplayName("HistoryEntry with integer as value")
    public void testHistoryEntry() {
        final var testValue = 4711;

        final var instantBeforeEntry = Instant.now();
        final var entry = new HistoryEntry<>(testValue);
        final var instantAfterEntry = Instant.now();

        assertThat(entry.getInstant()).isBetween(instantBeforeEntry, instantAfterEntry);
        assertThat(entry.getValue()).isEqualTo(testValue);
        assertThat(entry).hasToString(
                String.format("HistoryEntry{instant=%s, value=4711}", entry.getInstant())
        );
    }

    @Test
    @DisplayName("HistoryEntry with defined instant as value")
    public void testHistoryEntryWithDateTime() {
        final var testValue = "fooBar";

        final var dateTimeAsInstant = LocalDateTime.of(2018, 1, 2, 3, 4, 5, 6).toInstant(ZoneOffset.UTC);
        final var entry = new HistoryEntry<>(dateTimeAsInstant, testValue);

        assertThat(entry.getInstant()).isEqualTo("2018-01-02T03:04:05.000000006Z");
        assertThat(entry.getValue()).isEqualTo(testValue);
        assertThat(entry).hasToString("HistoryEntry{instant=2018-01-02T03:04:05.000000006Z, value=fooBar}");
    }

}
