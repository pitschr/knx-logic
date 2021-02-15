package li.pitschmann.knx.logic.exceptions;

/**
 * Creates a custom exception class for routing classes
 *
 * @author PITSCHR
 */
public class RouterException extends RuntimeException {

    /**
     * Creates a new {@link RouterException}  with the specified detail message.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public RouterException(final String message) {
        super(message);
    }

}
