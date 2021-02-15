package li.pitschmann.knx.logic.exceptions;

/**
 * Writer Exception
 */
public class WriterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public WriterException(final String message) {
        super(message);
    }

    public WriterException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
