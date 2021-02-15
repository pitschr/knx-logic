package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Strings;

import java.util.ArrayDeque;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * History keeping all recent history activities up to defined capacity
 * (or default {@value #DEFAULT_CAPACITY}).
 * <p>
 * After initialization the capacity cannot be changed anymore. History
 * keeps all historical values of the same type, so it is not possible to
 * mix up different instance types like {@link Integer} with {@link Boolean}.
 *
 * @param <T> type of value to be stored
 * @author PITSCHR
 */
public final class History<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private final int capacity;
    private final AtomicLong count = new AtomicLong();
    private final ArrayDeque<HistoryEntry<T>> historyEntries;

    /**
     * Constructor with default capacity (see: {@value #DEFAULT_CAPACITY})
     */
    public History() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructor with defined {@code capacity} for history.
     *
     * @param capacity the capacity how many history entries it can store at maximum
     */
    public History(final int capacity) {
        this.historyEntries = new ArrayDeque<>(capacity);
        this.capacity = capacity;
    }

    /**
     * Returns the last {@link HistoryEntry}.
     *
     * @return last {@link HistoryEntry}, or {@link NoSuchElementException} if history is empty.
     * @throws NoSuchElementException if history is empty
     */
    public HistoryEntry<T> getLast() {
        return historyEntries.getLast();
    }

    /**
     * Returns an immutable list of {@link HistoryEntry}.
     *
     * @return list with {@link HistoryEntry}
     */
    public List<HistoryEntry<T>> copyAsList() {
        return List.copyOf(historyEntries);
    }

    /**
     * Opens a stream based on an defense-copied {@link History} using {@link #copyAsList()} method.
     * It is equivalent to call using {@code copyAsList().stream()}.
     *
     * @return stream based on a defensive-copied history list
     */
    public Stream<HistoryEntry<T>> stream() {
        return copyAsList().stream();
    }

    /**
     * Returns list of values taken from {@link HistoryEntry#getValue()}
     * for all elements in current {@link History}
     *
     * @return immutable list of values
     */
    public List<T> values() {
        return stream().map(HistoryEntry::getValue).collect(Collectors.toUnmodifiableList());
    }

    /**
     * Adds a new {@link HistoryEntry} with current instant and given {@code value} to the history.
     * <p>
     * See: {@link HistoryEntry#HistoryEntry(Object)}
     *
     * @param value the value to be added as history entry; may not be null
     */
    public void addHistoryEntry(final T value) {
        Objects.requireNonNull(value);
        if (this.historyEntries.size() >= this.capacity) {
            this.historyEntries.remove();
        }
        this.historyEntries.addLast(new HistoryEntry<>(value));
        this.count.incrementAndGet();
    }

    /**
     * Returns the current capacity for {@link History} how many
     * {@link HistoryEntry} entries can be kept at <strong>maximum</strong>.
     * <p>
     * When adding more {@link HistoryEntry} then the oldest entry is removed.
     *
     * @return the current capacity of history
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Returns the current size of history.
     *
     * @return current size of history
     */
    public int size() {
        return this.historyEntries.size();
    }

    /**
     * <p>Returns how many entries have been added to the history.</p>
     *
     * <p>This method is more for diagnostic purposes only as history
     * will keep history entries up to pre-defined capacity.</p>
     *
     * <p>If given capacity is {@code 20} the count may be {@code 100}
     * which means that {@code 80} history values have been recorded since
     * beginning. Those old history values have already be evicted.</p>
     *
     * @return count of many history entries have been added in total
     */
    public long count() {
        return this.count.get();
    }

    /**
     * Removes all history entries.
     */
    public void clear() {
        this.historyEntries.clear();
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("size", this.historyEntries.size()) //
                .add("capacity", this.capacity) //
                .add("count", this.count) //
                .add("history", this.historyEntries) //
                .toString();
    }
}
