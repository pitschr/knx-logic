package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UIDAware;

/**
 * Interface for Component.
 *
 * @author PITSCHR
 */
public interface Component extends UIDAware {

    /**
     * Returns the wrapped object by the component
     *
     * @return the wrapped object; may not be null
     */
    Object getWrappedObject();

    /**
     * Returns the name of wrapped {@link Component} in following pattern:
     * <pre>{@code
     *      my.package.Logic
     * }</pre>
     *
     * @return name of Component
     */
    default String getName() {
        return getWrappedObject().getClass().getName();
    }
}
