package li.pitschmann.knx.logic.components;

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
     * Returns the simple name of wrapped {@link Component} in following pattern:
     * <pre>{@code
     *      Logic
     * }</pre>
     *
     * @return simple name of Component
     */
    default String getName() {
        return getWrappedObject().getClass().getSimpleName();
    }

    /**
     * Returns the absolute name of wrapped {@link Component} in following pattern:
     * <pre>{@code
     *      my.package.Logic
     * }</pre>
     *
     * @return absolute name of Component
     */
    default String getAbsoluteName() {
        return getWrappedObject().getClass().getName();
    }
}
