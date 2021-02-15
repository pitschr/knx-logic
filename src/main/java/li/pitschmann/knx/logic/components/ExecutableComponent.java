package li.pitschmann.knx.logic.components;

import java.util.concurrent.TimeUnit;

/**
 * Interface for Component
 *
 * @author PITSCHR
 */
public interface ExecutableComponent extends Component {
    /**
     * Executes the component.
     */
    void execute();

    /**
     * Returns a {@code long} number how many times this logic component was executed.
     *
     * @return number of execution; may be zero if not executed yet
     */
    long executedCount();

    /**
     * Returns a {@code long} number how long this component was executed
     * total in nanoseconds.
     *
     * @return {@code long} in nanoseconds; may be zero if not executed yet
     */
    long executedTime();

    /**
     * Returns a {@code long} number how long this component was executed
     * total in given {@code TimeUnit}
     *
     * @param unit the time unit that should be returned
     * @return {@code double} value; may be zero if not executed yet
     */
    default double executedTime(final TimeUnit unit) {
        return unit.convert(executedTime() * 1000, TimeUnit.NANOSECONDS) / 1000d;
    }

    /**
     * Returns a {@code double} number what the average execution time was
     * in nanoseconds (approximately). It doesn't consider special cases
     * like time spent in warm-up, initialization, etc.
     * <p>
     * The formula is: <code>average time = {@link #executedTime} / {@link #executedCount}</code>
     *
     * @return {@code double}; may be zero if not executed yet
     */
    default double executedAverageTime() {
        final var executedCount = executedCount();
        return executedCount == 0 ? 0d : ((double) executedTime()) / executedCount;
    }

    /**
     * Returns a {@code double} number what the average execution time was
     * in given {@link TimeUnit} approximately. It doesn't consider special
     * cases like time spent in warm-up, initialization, etc.
     * <p>
     * The formula is: <code>average time = {@link #executedTime} / {@link #executedCount}</code>
     *
     * @param unit the time unit that should be returned
     * @return {@code double}; may be zero if not executed yet
     */
    default double executedAverageTime(final TimeUnit unit) {
        final var executedAverageTime = executedAverageTime();
        if (executedAverageTime == 0d) {
            return 0d;
        } else if (unit == TimeUnit.NANOSECONDS) {
            return executedAverageTime;
        } else {
            return unit.convert((long) executedAverageTime * 1000, TimeUnit.NANOSECONDS) / 1000d;
        }
    }
}
