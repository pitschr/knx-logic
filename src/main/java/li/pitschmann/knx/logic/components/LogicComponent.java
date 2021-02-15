package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;

/**
 * Interface for Logic Component
 *
 * @author PITSCHR
 */
public interface LogicComponent extends ExecutableComponent,
        InputConnectorAware,
        OutputConnectorAware {
    /**
     * Returns a {@code long} number how many times the logic part
     * of component was invoked.
     *
     * @return number of invoked logic statement
     */
    long logicCount();
}
