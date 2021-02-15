package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.utils.Maps;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Channel for Variable events
 *
 * @author PITSCHR
 */
public final class VariableEventChannel implements EventChannel {
    public static final String CHANNEL_ID = "var";
    private final Map<String, Object> variableMap = Maps.newHashMap(100);

    @Override
    public String getChannel() {
        return CHANNEL_ID;
    }

    /**
     * Returns the cached value for given variable name
     *
     * @param name the name of variable as key to be look up in the variable map
     * @return the cached value or
     * @throws NoSuchElementException if no data could be found for given variable name
     */
    public Object getCachedValueFor(final String name) {
        final var data = variableMap.get(name);
        if (data == null) {
            throw new NoSuchElementException("Could not find data for variable name: " + name);
        }
        return data;
    }

    public static EventKey createKey(final String name) {
        return new EventKey(CHANNEL_ID, name);
    }

    @Override
    public void outbound(final Event event) {
        variableMap.put(
                event.getKey().getIdentifier(), //
                event.getData() //
        );
    }
}
