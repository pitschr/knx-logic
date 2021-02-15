package li.pitschmann.knx.logic.components.outbox;

/**
 * Interface for all {@link Outbox} components
 *
 * @author PITSCHR
 */
public interface Outbox {
    /**
     * Retrieves the value from logic pipeline and
     * prepares the value for the outbox
     *
     * @return the data for outbox
     */
    Object getData();
}
