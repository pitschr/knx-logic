package li.pitschmann.knx.logic.descriptor;

/**
 * Descriptor
 * <p>
 * Describes about the owner (Input, Output, Logic, Field, ...)
 */
public interface Descriptor {
    /**
     * Name of owner (Field, Class) that holds the descriptor
     *
     * @return name
     */
    String getName();
}
