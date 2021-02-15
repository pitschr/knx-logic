package li.pitschmann.knx.logic.connector;

import java.util.List;

/**
 * Factory returning list of {@link Connector} based on {@link ConnectorStrategy} implementation.
 *
 * <pre>{@code
 * final var component = new LogicComponentImpl(
 *                             new Logic() {
 *                                 @Input
 *                                 private int myField;
 *
 *                                 @Input
 *                                 private int myField2;
 *
 *                                 @Output
 *                                 private int myField3;
 *                                 ...
 *                             }
 *                       );
 * // unmodifiable list of input connectors (myField and myField2)
 * final var inputConnectors = ConnectorFactory.getInputConnectors(component);
 *
 * // unmodifiable list of output connector (myField3)
 * final var outputConnectors = ConnectorFactory.getOutputConnectors(component);
 * }</pre>
 *
 * @author PITSCHR
 */
public final class ConnectorFactory {
    private static final InputConnectorStrategy INPUT_CONNECTOR_STRATEGY = new InputConnectorStrategy();
    private static final OutputConnectorStrategy OUTPUT_CONNECTOR_STRATEGY = new OutputConnectorStrategy();

    private ConnectorFactory() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * Returns list of input {@link Connector} based on {@link #INPUT_CONNECTOR_STRATEGY}
     *
     * @param owner the component that owns the input connectors; may not be null
     * @return unmodifiable list of input {@link Connector}; empty otherwise
     */
    public static List<Connector> getInputConnectors(final Object owner) {
        return INPUT_CONNECTOR_STRATEGY.getConnectors(owner);
    }

    /**
     * Returns list of output {@link Connector} based on {@link #OUTPUT_CONNECTOR_STRATEGY}
     *
     * @param owner the component that owns the output connectors; may not be null
     * @return unmodifiable list of output {@link Connector}; empty otherwise
     */
    public static List<Connector> getOutputConnectors(final Object owner) {
        return OUTPUT_CONNECTOR_STRATEGY.getConnectors(owner);
    }
}
