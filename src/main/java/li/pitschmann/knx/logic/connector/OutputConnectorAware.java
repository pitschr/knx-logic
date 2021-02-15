package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.pin.OutputPinAware;
import li.pitschmann.knx.logic.pin.Pin;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Interface that current component is aware about output {@link Connector}
 *
 * @author PITSCHR
 */
public interface OutputConnectorAware extends ConnectorAware, OutputPinAware {
    /**
     * Returns an unmodifiable list of output {@link Connector}
     *
     * @return list of output {@link Connector}; may be empty if no output connectors are available
     */
    List<Connector> getOutputConnectors();

    /**
     * Returns output {@link Connector} for given {@code index}
     *
     * @param index the index of output connector to be returned
     * @return output {@link Connector}; not null
     * @throws IndexOutOfBoundsException if the index is out of the range
     */
    default Connector getOutputConnector(int index) {
        return getOutputConnectors().get(index);
    }

    /**
     * Returns output {@link Connector} by its field name.
     * <p>
     * This method will iterate through all input connectors.
     * Performance-wise you should try to find the connector
     * by its index.
     *
     * @param name the name of field / connector to look up for
     * @return output {@link Connector}; not null
     * @throws NoSuchElementException if no such element with the given field name could be found
     */
    default Connector getOutputConnector(final String name) {
        Preconditions.checkNonNull(name);
        return getOutputConnectors()
                .stream()
                .filter(c -> name.equals(c.getDescriptor().getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Output Connector with field name found: " + name));
    }

    @Override
    default List<Pin> getOutputPins() {
        return getOutputConnectors().stream().flatMap(Connector::getPinStream)
                .collect(Collectors.toUnmodifiableList());
    }
}
