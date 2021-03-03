package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.helpers.ValueHelper;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Descriptor for annotated fields.
 *
 * @author PITSCHR
 */
public class FieldDescriptor implements Descriptor {
    private final Object owner;
    private final Field field;
    private final Class<?> fieldType;

    /**
     * Package-private Constructor for {@link FieldDescriptor}
     *
     * @param owner the owner that holds the field; may not be null
     * @param field the field that should be described; may not be null
     */
    FieldDescriptor(final Object owner, final Field field) {
        this.owner = Objects.requireNonNull(owner);
        this.field = Objects.requireNonNull(field);
        this.fieldType = Objects.requireNonNull(ValueHelper.getFieldType(field));
    }

    /**
     * Returns the {@link Field} that is represented by the {@link FieldDescriptor}
     *
     * @return the field instance; may not be null
     */
    public final Field getField() {
        return field;
    }

    /**
     * Returns the name of {@link Field} that is represented by the {@link FieldDescriptor}.
     * It is a handy method for {@link #getField()#getName()}.
     *
     * @return name of field; may not be null
     */
    @Override
    public final String getName() {
        return field.getName();
    }

    /**
     * Returns the type of class that is represented by the field
     * <p>
     * Examples:<br>
     * {@code private List<Boolean> myField;} would be {@code Boolean}<br>
     * {@code private Boolean myField;}would be {@code Boolean}<br>
     *
     * @return type of field value; may not be null
     */
    public Class<?> getFieldType() {
        return fieldType;
    }

    /**
     * Returns the owner of current {@link Descriptor}
     *
     * @return the owner of field; may not be null
     */
    public Object getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("owner", owner) //
                .add("field", field) //
                .add("fieldType", fieldType) //
                .toString();
    }
}
