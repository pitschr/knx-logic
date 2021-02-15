package li.pitschmann.knx.logic.connector;

import java.util.List;

/**
 * Implementation for strategy to fetch all connectors
 *
 * @author PITSCHR
 */
public interface ConnectorStrategy {
    /**
     * Returns an unmodifiable list of {@link Connector} for given {@code owner}
     *
     * @param owner the object that owns the list of connectors; may not be null
     * @return list of {@link Connector}, or empty list
     */
    List<Connector> getConnectors(final Object owner);
}
