package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.annotations.Output;

/**
 * {@link ConnectorStrategy} for {@link Output} fields
 *
 * @author PITSCHR
 */
public final class OutputConnectorStrategy extends AbstractConnectorStrategy {

    /**
     * Returns the {@link Output} class as annotation class for look-up
     *
     * @return {@link Output} class
     */
    @Override
    protected Class<Output> getAnnotationClass() {
        return Output.class;
    }
}
