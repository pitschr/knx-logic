package li.pitschmann.knx.logic.transformers;

import li.pitschmann.knx.core.annotations.Nullable;

/**
 * Interface for transformers that allows
 * to transform from a string value to a
 * instance of e.g. Integer, Date, ...
 *
 * @param <T>
 * @author PITSCHR
 */
public interface Transformer<T> {
    /**
     * Returns the compatible class that is supported by the current class
     *
     * @return the target class, which is an instance of {@code T}
     */
    Class<T> getTargetClass();

    /**
     * Transforms from {@link String} to {@code T}
     *
     * @param value string value
     * @return {@code T}
     */
    T transform(final @Nullable String value);

    /**
     * Returns the default value for class {@code T}
     *
     * @return default value for {@code T}
     */
    T getDefaultValue();
}
