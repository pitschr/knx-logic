package li.pitschmann.knx.logic.components.inbox;

/**
 * Interface for all {@link Inbox} components
 *
 * @author PITSCHR
 */
public interface Inbox {
    /**
     * Accepts data as {@link Object} that may contain data
     * (e.g. byte array) to set the value(s) for pins that
     * is used for the logic pipeline.
     *
     * @param data object to be accepted
     */
    void accept(final Object data);
}
