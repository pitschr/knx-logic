package li.pitschmann.knx.logic.uid;

/**
 * Interface indicating current instance is aware about an unique id
 */
public interface UIDAware {

    /**
     * Get unique identifier
     *
     * @return {@link UID}; may not be null
     */
    UID getUid();

}
