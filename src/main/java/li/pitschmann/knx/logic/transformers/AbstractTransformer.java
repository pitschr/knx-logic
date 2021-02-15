package li.pitschmann.knx.logic.transformers;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Strings;

/**
 * Abstract Transformer as base class to guarantee null-safety
 *
 * @param <T> the type of the transformed value
 * @author PITSCHR
 */
public abstract class AbstractTransformer<T> implements Transformer<T> {

    @Override
    public T transform(final @Nullable String value) {
        return Strings.isNullOrEmpty(value) ? this.getDefaultValue() : this.transformNullSafe(value);
    }

    /**
     * Transforms the given {@code value} which is NOT a null to an instance of {@code T}. The null-check is done in
     * {@link #transform(String)} already.
     *
     * @param value the value to be transformed to an instance of {@code T}
     * @return transformed value, which is an instance of {@code T}
     */
    protected abstract T transformNullSafe(final String value);

}
