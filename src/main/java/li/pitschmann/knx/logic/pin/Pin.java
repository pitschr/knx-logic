package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.uid.UIDAware;

/**
 * Interface for pin
 *
 * @author PITSCHR
 */
public interface Pin extends UIDAware {
    /**
     * Returns the value of current {@link Pin}
     *
     * @return value; may be null
     */
    Object getValue();

    /**
     * Sets the value of current {@link Pin}
     *
     * @param value the value to be set; may be null
     */
    void setValue(final Object value);

    /**
     * Returns if the current {@link Pin} is marked as refreshed
     *
     * @return {@code true} if refreshed, otherwise {@code false}
     */
    boolean isRefresh();

    /**
     * Mark the current {@link Pin} as refreshed
     */
    void setRefresh();

    /**
     * Clears the refresh flag for current {@link Pin}
     */
    void clearRefresh();

    /**
     * Returns the count how often the pin has been refreshed
     *
     * @return count of pin being refreshed
     */
    long refreshCount();

    /**
     * Returns if the trigger of pin is marked with 'always'
     *
     * @return {@code true} if alwaysTrigger, otherwise {@code false}
     */
    boolean isAlwaysTrigger();

    /**
     * Gets the {@link Connector}
     *
     * @return the connector
     */
    Connector getConnector();

    /**
     * Returns the {@link FieldDescriptor} of current {@link Pin}
     *
     * @return descriptor
     */
    default FieldDescriptor getDescriptor() {
        return getConnector().getDescriptor();
    }

    /**
     * Returns the name of {@link Pin} in following pattern:
     * <pre>{@code
     *      inputField
     * }</pre>
     *
     * @return name of Pin
     */
    default String getName() {
        return getDescriptor().getField().getName();
    }

    /**
     * Returns the absolute name of {@link Pin} in following pattern:
     * <pre>{@code
     *      my.package.Logic#inputField
     * }</pre>
     *
     * @return name of Pin
     */
    default String getAbsoluteName() {
        return getDescriptor().getOwner().getClass().getName() + "#" + getName();
    }
}
