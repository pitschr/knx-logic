package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.pin.InputPinAware;
import li.pitschmann.knx.logic.pin.Pin;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Interface that current component is aware about input {@link Connector}
 *
 * @author PITSCHR
 */
public interface InputConnectorAware extends ConnectorAware, InputPinAware {
    /**
     * Returns an unmodifiable list of input {@link Connector}
     *
     * @return list of input {@link Connector}; may be empty if no input connectors are available
     */
    List<Connector> getInputConnectors();

    /**
     * Returns input {@link Connector} for given {@code index}
     *
     * @param index the index of input connector to be returned
     * @return input {@link Connector}; not null
     * @throws IndexOutOfBoundsException if the index is out of the range
     */
    default Connector getInputConnector(final int index) {
        return getInputConnectors().get(index);
    }

    /**
     * Returns input {@link Connector} by its field name.
     * <p>
     * This method will iterate through all input connectors.
     * Performance-wise you should try to find the connector
     * by its index.
     *
     * @param name the name of field / connector to look up for
     * @return input {@link Connector}; not null
     * @throws NoSuchElementException if no such element with the given field name could be found
     */
    default Connector getInputConnector(final String name) {
        Preconditions.checkNonNull(name);
        return getInputConnectors()
                .stream()
                .filter(c -> name.equals(c.getDescriptor().getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Input Connector with field name found: " + name));
    }

    @Override
    default List<Pin> getInputPins() {
        return getInputConnectors().stream().flatMap(Connector::getPinStream)
                .collect(Collectors.toUnmodifiableList());
    }
}
