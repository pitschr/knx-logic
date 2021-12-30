package li.pitschmann.knx.logic.diagram;

import li.pitschmann.knx.logic.uid.UIDAware;

/**
 * @author PITSCHR
 */
public interface Diagram extends UIDAware {
    /**
     * Returns the name of diagram
     *
     * @return name
     */
    String getName();

    /**
     * Returns the description of diagram
     *
     * @return description
     */
    String getDescription();
}
