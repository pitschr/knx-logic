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

}
