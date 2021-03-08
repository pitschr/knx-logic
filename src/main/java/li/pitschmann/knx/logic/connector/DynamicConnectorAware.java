package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.exceptions.MaximumBoundException;
import li.pitschmann.knx.logic.exceptions.MinimumBoundException;
import li.pitschmann.knx.logic.pin.DynamicPin;

import java.util.List;

/**
 * Interface for {@link DynamicPin} awareness
 *
 * @author PITSCHR
 */
public interface DynamicConnectorAware {
    /**
     * Creates a new {@link DynamicPin} and appends it at the end of the list
     *
     * @return newly created {@link DynamicPin}; not null
     * @throws MaximumBoundException in case the maximum bound has already been reached
     */
    DynamicPin addPin();

    /**
     * Creates and adds a new {@link DynamicPin} at given {@code index}
     *
     * @param index the index of {@link DynamicPin} to be added; must be in valid size range
     * @return newly created {@link DynamicPin}; not null
     * @throws MaximumBoundException in case the maximum bound has already been reached
     */
    DynamicPin addPin(int index);

    /**
     * Returns an unmodifiable list of all {@link DynamicPin}
     *
     * @return list of {@link DynamicPin}; may be empty; not null
     */
    List<DynamicPin> getPins();

    /**
     * Returns the {@link DynamicPin} at given {@code index}
     *
     * @param index the index of {@link DynamicPin} to return
     * @return existing {@link DynamicPin}; not null
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    DynamicPin getPin(int index);

    /**
     * Removes the {@link DynamicPin} at given {@code index}
     *
     * @param index the index of {@link DynamicPin} to be removed
     * @return the {@link DynamicPin} instance which has been removed; not null
     * @throws MinimumBoundException in case the minimum bound has already been reached
     */
    DynamicPin removePin(int index);

    /**
     * Resets the {@link DynamicConnector} by removing all existing
     * {@link DynamicPin}s and then re-initialize with minimum size
     */
    void reset();

    /**
     * Returns the size of all existing {@link DynamicPin}s
     *
     * @return the size of {@link DynamicPin}s
     */
    int size();

    /**
     * Increases the size of {@link DynamicPin} to {@code desiredSize} and returns
     * an unmodifiable list of {@link DynamicPin} that were newly created and added
     * to the list.
     *
     * @param desiredSize the desired size of the {@link DynamicConnector}
     * @return list of {@link DynamicPin} that were newly created, the list
     * may be empty if the size could not be increased due e.g. maximum
     * occurrence or if the component has more fields than desired size already
     */
    List<DynamicPin> tryIncrease(int desiredSize);
}
