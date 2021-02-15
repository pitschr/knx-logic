package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Strings;

import java.time.Instant;
import java.util.Objects;

/**
 * Data holder for value history containing the instant and value.
 * <p>
 * This class is immutable.
 *
 * @param <T> value instance type
 * @author PITSCHR
 */
public final class HistoryEntry<T> {
    private final Instant instant;
    private final T value;

    /**
     * Constructor for {@link HistoryEntry}.
     * <p>
     * The {@link #getInstant()} ()} will be current instant
     *
     * @param value the value to be stored for history purposes; may not be null
     */
    public HistoryEntry(final T value) {
        this(Instant.now(), value);
    }

    /**
     * Constructor for {@link HistoryEntry}.
     *
     * @param instant the instant when history entry was created
     * @param value   the value to be stored for history purposes; may not be null
     */
    public HistoryEntry(final Instant instant, final T value) {
        this.instant = Objects.requireNonNull(instant);
        this.value = Objects.requireNonNull(value);
    }

    /**
     * Returns the instant that corresponds when this entry
     * was created.
     *
     * @return the {@link Instant}; may not be null
     */
    public Instant getInstant() {
        return instant;
    }

    /**
     * Returns the value
     *
     * @return value which is an instance of {@code T}; may not be null
     */
    public T getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("instant", this.instant) //
                .add("value", this.value) //
                .toString();
    }
}
