package li.pitschmann.knx.logic.helpers;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.exceptions.ReflectException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Common helper for reflection
 *
 * @author PITSCHR
 */
public final class ReflectHelper {

    private ReflectHelper() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * Filters the list of {@link Field} by given {@code annotation} class
     *
     * @param fields     array of field that should be looked up for suitable annotation class
     * @param annotation the class of annotation for look up
     * @return list of {@code Field} which contains {@code annotation} class, empty list if none found
     */
    public static List<Field> filterFieldsByAnnotation(final Field[] fields, final Class<? extends Annotation> annotation) {
        return Stream.of(fields).filter(field -> field.isAnnotationPresent(annotation)).collect(Collectors.toList());
    }

    /**
     * Returns {@link Field} instance for given {@code fieldName} and {@code clazz}. It will iterate through whole class
     * hierarchy.
     *
     * @param clazz     the class that keeps the field with {@code fieldName}
     * @param fieldName the field name to be found
     * @return {@code Field} instance, otherwise {@link ReflectException}
     * @throws ReflectException if no field with given criteria could be found
     */
    private static Field findField(final Class<?> clazz, final String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException e) {
            var clazz0 = clazz;
            // not found - might be OK, try one class level deeper!
            while ((clazz0 = clazz0.getSuperclass()) != null) {
                try {
                    field = clazz0.getDeclaredField(fieldName);
                } catch (final NoSuchFieldException ignore) {
                    // ignore
                }
            }
        }

        if (field == null) {
            throw new ReflectException(String.format("Could not find field name '%s' in class hierarchy of class '%s'", fieldName, clazz));
        }

        return field;
    }

    /**
     * Sets the value using reflection for given {@code owner} instance and {@code fieldName}. In case when
     * {@code fieldName} exists twice times in class hierarchy then first one is returned.
     * <p><strong>This bypasses the security check for private fields!</strong></p>
     *
     * @param owner     the instance that keeps the field with {@code fieldName}
     * @param fieldName the field name to be set
     * @param newValue  the new value
     */
    public static void setInternalValue(final Object owner, final String fieldName, final @Nullable Object newValue) {
        setInternalValue(owner, findField(owner.getClass(), fieldName), newValue);
    }

    /**
     * Sets the value using reflection for given {@code owner} instance and {@code field}.
     * <p><strong>This bypasses the security check for private fields!</strong></p>
     *
     * @param owner    the instance that keeps the field
     * @param field    the field to be set
     * @param newValue the new value
     */
    @SuppressWarnings("squid:S3011")
    public static void setInternalValue(final Object owner, final Field field, final @Nullable Object newValue) {
        try {
            accessible(field).set(owner, newValue);
        } catch (final IllegalArgumentException | ReflectiveOperationException e) {
            throw new ReflectException(String.format("Could not set value '%s' for owner '%s' and field '%s'", newValue, owner, field), e);
        }
    }

    /**
     * Sets the value using reflection for given {@code owner} instance and {@code fieldName}.
     *
     * @param owner     the instance that keeps the field with {@code fieldName}
     * @param fieldName the field name that holds the value
     * @return value, may be {@code null}
     */
    @Nullable
    public static <T> T getInternalValue(final Object owner, final String fieldName) {
        return getInternalValue(owner, findField(owner.getClass(), fieldName));
    }

    /**
     * Sets the value using reflection for given {@code owner} instance and {@code field}.
     *
     * @param owner the instance that keeps the field
     * @param field the field that holds the value
     * @return value, may be {@code null}
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> T getInternalValue(final Object owner, final Field field) {
        try {
            return (T) accessible(field).get(owner);
        } catch (final IllegalArgumentException | ReflectiveOperationException e) {
            throw new ReflectException(String.format("Could not get value for owner '%s' and field '%s'", owner, field), e);
        }
    }

    /**
     * Ensure that {@link AccessibleObject} is accessible
     *
     * @param accessible try to given {@link AccessibleObject} to be accessible
     * @return {@link AccessibleObject} itself
     */
    private static <T extends AccessibleObject> T accessible(final T accessible) {
        Preconditions.checkArgument(accessible.trySetAccessible());
        return accessible;
    }

}
