package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.annotations.Input;

import java.lang.reflect.Field;

/**
 * {@link DescriptorStrategy} for {@link Input} fields
 *
 * @author PITSCHR
 */
public final class InputDescriptorStrategy implements FieldDescriptorStrategy {

    @Override
    public InputDescriptor createDescriptor(final Object owner, final Field field) {
        final var annotation = field.getAnnotation(Input.class);
        return new InputDescriptor(owner, annotation, field);
    }
}
