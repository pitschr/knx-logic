package li.pitschmann.knx.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test case for {@link History}
 *
 * @author PITSCHR
 */
public final class HistoryTest {

    @Test
    @DisplayName("Empty History")
    public void testEmptyHistory() {
        final var history = new History<>();

        assertThat(history.count()).isZero();
        assertThat(history.size()).isZero();
        assertThat(history.values()).isEmpty();
        assertThatThrownBy(history::getLast).isInstanceOf(NoSuchElementException.class);

        assertThat(history).hasToString("History{size=0, capacity=10, count=0, history=[]}");
    }

    @Test
    @DisplayName("History with default capacity and do not overflow it")
    public void testHistoryDefaultCapacity() {
        final var history = new History<String>();
        for (var i = 1; i <= history.capacity(); i++) {
            history.addHistoryEntry("A-" + i);
        }

        final var expectedFirstValue = "A-1";
        final var expectedLastValue = "A-" + history.capacity();

        assertThat(history.size()).isEqualTo(history.capacity());
        assertThat(history.copyAsList().get(0).getValue()).isEqualTo(expectedFirstValue);
        assertThat(history.getLast().getValue()).isEqualTo(expectedLastValue);
        assertThat(history.stream().count()).isEqualTo(history.capacity());
        assertThat(history.count()).isEqualTo(history.capacity());
    }

    @Test
    @DisplayName("History with default capacity and overflow it")
    public void testHistoryDefaultCapacityAndOverflow() {
        final var numberOverflow = 10;

        final var history = new History<String>();
        for (var i = 1; i <= history.capacity() + numberOverflow; i++) {
            history.addHistoryEntry("B-" + i);
        }

        final var expectedCount = history.capacity() + numberOverflow;
        final var expectedFirstValue = "B-" + (numberOverflow + 1);
        final var expectedLastValue = "B-" + expectedCount;

        assertThat(history.size()).isEqualTo(history.capacity());
        assertThat(history.copyAsList().get(0).getValue()).isEqualTo(expectedFirstValue);
        assertThat(history.getLast().getValue()).isEqualTo(expectedLastValue);
        assertThat(history.stream().count()).isEqualTo(history.capacity());
        assertThat(history.count()).isEqualTo(expectedCount);
    }

    @Test
    @DisplayName("Initialize history with capacity (5) and add three entries")
    public void testHistoryWithInitialSize() {
        final var initialCapacity = 5;

        final var history = new History<String>(initialCapacity);
        // add only 3 items (do not fill the history)
        history.addHistoryEntry("A");
        history.addHistoryEntry("B");
        history.addHistoryEntry("C");

        assertThat(history.size()).isEqualTo(3);
        assertThat(history.copyAsList().get(0).getValue()).isEqualTo("A");
        assertThat(history.getLast().getValue()).isEqualTo("C");
        assertThat(history.stream().count()).isEqualTo(3);
        assertThat(history.count()).isEqualTo(3);
        assertThat(history.values()).containsExactly("A", "B", "C");
    }

    @Test
    @DisplayName("Initialize history with capacity (200) and overflow it")
    public void testHistoryWithInitialSizeOverflow() {
        final var initialCapacity = 200;
        final var numberOverflow = 110;

        final var history = new History<>(initialCapacity);
        for (var i = 1; i <= initialCapacity + numberOverflow; i++) {
            history.addHistoryEntry(i);
        }

        assertThat(history.size()).isEqualTo(initialCapacity);
        assertThat(history.copyAsList().get(0).getValue()).isEqualTo(111);
        assertThat(history.getLast().getValue()).isEqualTo(310);
        assertThat(history.stream().count()).isEqualTo(200);
        assertThat(history.count()).isEqualTo(310);
    }

    @Test
    @DisplayName("Fill history with some data and then then clear it")
    public void testHistoryFillAndClear() {
        final var history = new History<String>(3);
        history.addHistoryEntry("D");
        history.addHistoryEntry("E");
        history.addHistoryEntry("F");

        assertThat(history.size()).isEqualTo(3);
        assertThat(history.count()).isEqualTo(3);
        assertThat(history.getLast()).isNotNull();
        assertThat(history.getLast().getValue()).isEqualTo("F");

        // clear it
        history.clear();

        assertThat(history.size()).isZero();
        assertThat(history.count()).isEqualTo(3);
        assertThatThrownBy(history::getLast).isInstanceOf(NoSuchElementException.class);

        // add one entry
        history.addHistoryEntry("G");

        assertThat(history.size()).isEqualTo(1);
        assertThat(history.count()).isEqualTo(4);
        assertThat(history.getLast().getValue()).isEqualTo("G");
        assertThat(history.values()).containsExactly("G");
    }

    @Test
    @DisplayName("History with toString()")
    public void testToString() {
        final var history = new History<String>(7);
        history.addHistoryEntry("H"); // size: 1, count: 1
        history.clear();                    // size: 0, count: 1
        history.addHistoryEntry("I"); // size: 1, count: 2
        history.addHistoryEntry("J"); // size: 2, count: 3

        assertThat(history).hasToString(
                String.format("History{size=2, capacity=7, count=3, history=%s}", history.copyAsList())
        );
    }

    @Test
    @DisplayName("Use stream and values with History")
    public void testStreamAndValues() {
        final var history = new History<String>(3);
        history.addHistoryEntry("K");
        history.addHistoryEntry("L");
        history.addHistoryEntry("M");
        history.addHistoryEntry("N");

        assertThat(history.stream().map(HistoryEntry::getValue)).containsExactly("L", "M", "N");
        assertThat(history.values()).containsExactly("L", "M", "N");
    }
}
