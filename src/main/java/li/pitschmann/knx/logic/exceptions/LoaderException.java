package li.pitschmann.knx.logic.exceptions;

/**
 * Loader Exception
 */
public class LoaderException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LoaderException(final String message) {
        super(message);
    }

    public LoaderException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
