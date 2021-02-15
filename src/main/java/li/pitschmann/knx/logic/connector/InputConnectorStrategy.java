package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.annotations.Input;

/**
 * {@link ConnectorStrategy} for {@link Input} fields
 *
 * @author PITSCHR
 */
public final class InputConnectorStrategy extends AbstractConnectorStrategy {

    /**
     * Returns the {@link Input} class as annotation class for look-up
     *
     * @return {@link Input} class
     */
    @Override
    protected Class<Input> getAnnotationClass() {
        return Input.class;
    }
}
