package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.pin.StaticPin;

/**
 * Interface for {@link StaticPin} awareness
 *
 * @author PITSCHR
 */
public interface StaticConnectorAware {
    /**
     * Returns the {@link StaticPin}
     *
     * @return {@link StaticPin}; not null
     */
    StaticPin getPin();
}
