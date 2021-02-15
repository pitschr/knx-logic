package li.pitschmann.knx.logic;

/**
 * Interface for logic as component
 *
 * @author PITSCHR
 */
public interface Logic {
    /**
     * <strong>Start</strong> of the logic.
     * Will be executed every time!
     */
    default void start() {
        // NO-OP
    }

    /**
     * <strong>Initializes</strong> the logic.
     * Will be executed only on first time!
     */
    default void init() {
        // NO-OP
    }

    /**
     * Actions for <strong>logic</strong>.
     * Will be executed depending on trigger setting!
     * <p>
     * See: {@link li.pitschmann.knx.logic.Trigger}
     */
    void logic();

    /**
     * <strong>End</strong> of the logic.
     * Will be executed every time!
     */
    default void end() {
        // NO-OP
    }
}
