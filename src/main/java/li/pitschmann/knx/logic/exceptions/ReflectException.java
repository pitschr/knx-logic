package li.pitschmann.knx.logic.exceptions;

/**
 * Wrapper for reflection exceptions
 */
public class ReflectException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReflectException(final String message) {
        super(message);
    }

    public ReflectException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
