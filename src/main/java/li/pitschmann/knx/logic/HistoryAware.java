package li.pitschmann.knx.logic;

/**
 * Classes which implements this interface are history-capable.
 * <p>
 * This means it will record all values and stores in {@link HistoryEntry}
 * containing the date and time and the value.
 * <p>
 * This is useful if you want to record how the values have
 * been changed and is very useful for diagnosis.
 *
 * @param <T> instance type of value
 * @author PITSCHR
 */
public interface HistoryAware<T> {
    /**
     * Returns the {@link History}
     *
     * @return {@link History} which contains values which
     * are an instance of {@code T}; may not be null
     */
    History<T> getHistory();
}
