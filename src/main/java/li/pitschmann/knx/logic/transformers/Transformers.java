package li.pitschmann.knx.logic.transformers;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.core.utils.Preconditions;

import java.util.Map;

/**
 * Manager for {@link Transformer} to find the suitable transformer implementation
 * to transform a string value to an value of a different class.
 *
 * <u>Example:</u>
 * <ul>
 *     <li>"1", "true", "TRUE", could be transformed to {@link Boolean#TRUE}<br></li>
 *     <li>"0", "false", "FALSE", any other string, could be transformed to {@link Boolean#FALSE}<br></li>
 * </ul>
 * <p>
 * This allows to implement more strategies than e.g. {@link Boolean#valueOf(String)}
 * which would accept only {@code true} and {@code false}. And it is also null-safe
 * which returns a default value in case of {@code null}. As example, for {@code int}
 * and {@code char} it would be {@code 0} and {@code \}{@code u}{@code 0000}, respectively).
 */
public final class Transformers {
    private static final Map<Class<?>, Transformer<?>> TRANSFORMER_MAP = Maps.newHashMap(10);

    static {
        registerTransformers(new BooleanTransformer());
        registerTransformers(new ByteTransformer());
        registerTransformers(new CharacterTransformer());
        registerTransformers(new DoubleTransformer());
        registerTransformers(new FloatTransformer());
        registerTransformers(new IntegerTransformer());
        registerTransformers(new LongTransformer());
        registerTransformers(new ShortTransformer());
        registerTransformers(new StringTransformer());
        registerTransformers(new BigIntegerTransformer());
        registerTransformers(new BigDecimalTransformer());
    }

    private Transformers() {
        throw new AssertionError("Don't touch me!");
    }

    private static void registerTransformers(final Transformer<?> transformer) {
        TRANSFORMER_MAP.put(transformer.getTargetClass(), transformer);
    }

    /**
     * Transforms given {@code value}. It will look up for suitable {@link Transformer}
     * based on {@code targetClass} and returns the transformed value
     *
     * @param value       the value to be transformed; may be null
     * @param targetClass the class that should transform the value to be an instance of this class
     * @return transformed object which is an instance of {@code T}
     * @throws IllegalArgumentException if no suitable {@link Transformer} could be found
     */
    public static <T> T transform(final @Nullable String value, final Class<T> targetClass) {
        Preconditions.checkArgument(!targetClass.isPrimitive(),
                "No transformation for primitive classes. Use wrapper classes.");

        @SuppressWarnings("unchecked") final var transformer = (Transformer<T>) TRANSFORMER_MAP.get(targetClass);
        if (transformer == null) {
            throw new IllegalArgumentException("Could not find a matching transformer for: " + targetClass.getName());
        }
        return transformer.transform(value);
    }
}
