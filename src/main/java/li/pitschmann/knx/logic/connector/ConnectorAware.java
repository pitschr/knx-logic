package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.PinAware;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Marker Interface for instances that are aware of {@link Connector}
 *
 * @author PITSCHR
 */
public interface ConnectorAware extends PinAware {
    /**
     * Returns an unmodifiable list of all available {@link Connector}s
     *
     * @return list of {@link Connector}; may be empty if no connectors are available
     */
    List<Connector> getConnectors();

    /**
     * Returns {@link Connector} by its field name.
     *
     * @param name the name of field / connector to look up for
     * @return input {@link Connector}; cannot be null
     * @throws NoSuchElementException if no such element with the given field name could be found
     */
    default Connector getConnector(final String name) {
        Preconditions.checkNonNull(name);
        return getConnectors()
                .stream()
                .filter(c -> name.equals(c.getDescriptor().getName()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Connector with field name found: " + name));
    }

    @Override
    default List<Pin> getPins() {
        return getConnectors().stream().flatMap(Connector::getPinStream)
                .collect(Collectors.toUnmodifiableList());
    }
}
