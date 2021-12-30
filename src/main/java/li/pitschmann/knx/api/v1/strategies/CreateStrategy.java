package li.pitschmann.knx.api.v1.strategies;

import java.util.Map;

/**
 * Create strategy for {@code <T>}
 *
 * @param <T> instance of T
 * @author PITSCHR
 */
public interface CreateStrategy<T> {

    /**
     * Creates a new instance of {@code <T>} based on
     * {@code data}
     *
     * @param data map with data that is need to create an instance of {@code <T>}
     * @return a new inbox component
     */
    T apply(final Map<String, String> data);

}
