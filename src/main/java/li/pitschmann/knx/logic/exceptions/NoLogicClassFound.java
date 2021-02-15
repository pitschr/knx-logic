package li.pitschmann.knx.logic.exceptions;

/**
 * If no Logic class could be found
 */
public class NoLogicClassFound extends RuntimeException {
    public NoLogicClassFound(final String message) {
        super(message);
    }
}
