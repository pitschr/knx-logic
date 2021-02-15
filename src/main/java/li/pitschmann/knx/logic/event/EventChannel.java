package li.pitschmann.knx.logic.event;

/**
 * Interface for Event Channel that is used for routing purposes.
 */
public interface EventChannel {
    /**
     * Returns the channel name of associated {@link EventChannel}
     *
     * @return channel name; may not be null
     */
    String getChannel();

    /**
     * Invokes the outbound procedure
     *
     * @param event the event that contains data for outbound; may not be null
     */
    void outbound(final Event event);
}
