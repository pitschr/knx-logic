package li.pitschmann.knx.logic.helpers;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.core.utils.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Objects;

/**
 * Value Helper
 *
 * @author PITSCHR
 */
public final class ValueHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ValueHelper.class);
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap;

    static {
        primitiveWrapperMap = Maps.newHashMap(10);
        primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveWrapperMap.put(Character.TYPE, Character.class);
        primitiveWrapperMap.put(Double.TYPE, Double.class);
        primitiveWrapperMap.put(Float.TYPE, Float.class);
        primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveWrapperMap.put(Long.TYPE, Long.class);
        primitiveWrapperMap.put(Short.TYPE, Short.class);
        primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
    }

    private ValueHelper() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * Returns the default value for given {@code clazz}.
     *
     * @param clazz
     * @return default value which belongs to {@code Class<T>}, otherwise {@code null}
     */
    @Nullable
    public static <T> T getDefaultValueFor(final Class<T> clazz) {
        Preconditions.checkNonNull(clazz, "No class provided");
        Preconditions.checkArgument(!clazz.isPrimitive(), "Class should not be a primitive");

        final Object result;
        if (Boolean.class == clazz) {
            result = Boolean.FALSE;
        } else if (Integer.class == clazz) {
            result = 0;
        } else if (Long.class == clazz) {
            result = 0L;
        } else if (Float.class == clazz) {
            result = .0f;
        } else if (Double.class == clazz) {
            result = .0d;
        } else if (Byte.class == clazz) {
            result = (byte) 0;
        } else if (Short.class == clazz) {
            result = (short) 0;
        } else if (Number.class == clazz) {
            result = 0;
        } else if (Character.class == clazz) {
            result = Character.MIN_VALUE;
        } else if (String.class == clazz) {
            result = "";
        } else {
            result = null;
            LOG.debug("No default value found for class '{}'. Return null.", clazz);
        }

        return result == null ? null : clazz.cast(result);
    }

    /**
     * Returns the field type for given {@code field}. In case the class is a primitive, the wrapper of primitive
     * is returned.
     * <p>
     * Examples:<br>
     * <code>private String test;</code> will return <code>java.lang.String</code><br/>
     * <code>private List&lt;String&gt; tests;</code> will return <code>java.lang.String</code> as well.<br/>
     * <code>private boolean test;</code> will return <code>java.lang.Boolean</code><br/>
     * <code>private List&lt;Boolean&gt; tests;</code> will return <code>java.lang.Boolean</code> as well.<br/>
     *
     * @param field the field
     * @return Class if found, otherwise null
     */
    public static Class<?> getFieldType(final Field field) {
        return getFieldType(field, 0);
    }

    /**
     * Returns the field type from given {@code index} from {@code field}. In case the class is a primitive, the
     * wrapper of primitive is returned.
     * <p>
     * Examples:<br>
     * <code>private String test;</code> will return <code>java.lang.String</code><br/>
     * <code>private List&lt;String&gt; tests;</code> will return <code>java.lang.String</code> as well.<br/>
     * <code>private boolean test;</code> will return <code>java.lang.Boolean</code><br/>
     * <code>private List&lt;Boolean&gt; tests;</code> will return <code>java.lang.Boolean</code> as well.<br/>
     *
     * @param field the field
     * @param index the index of field
     * @return Class if found, otherwise null
     */
    private static Class<?> getFieldType(final Field field, final int index) {
        Class<?> clazz = null;
        final var genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            final var type = ((ParameterizedType) genericType).getActualTypeArguments();
            if (type[index] instanceof Class) {
                // Example: private List<Integer> fieldName
                clazz = (Class<?>) type[index];
            } else if (type[index] instanceof TypeVariable) {
                // Example: private List<T> fieldName
                final var typeVariable = (TypeVariable<?>) type[index];
                clazz = (Class<?>) typeVariable.getBounds()[0];
            }
        } else {
            // Example: private int fieldName
            // Example: private Integer fieldName
            clazz = field.getType();
        }

        // class found?
        if (clazz == null) {
            throw new UnsupportedOperationException(
                    String.format("No value class found for field '%s' and index '%s'", field, index)
            );
        }

        // convert from primitive to wrapper if necessary
        if (clazz.isPrimitive()) {
            final var convertedClazz = Objects.requireNonNull(primitiveWrapperMap.get(clazz));
            LOG.debug("Primitive conversion from '{}' to wrapper class '{}'.", clazz, convertedClazz);
            return convertedClazz;
        } else {
            return clazz;
        }
    }

}
