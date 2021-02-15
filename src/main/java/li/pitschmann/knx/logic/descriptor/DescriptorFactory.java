package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Factory for {@link Descriptor} that will returns it based on {@link DescriptorStrategy} implementation.
 *
 * @author PITSCHR
 */
public final class DescriptorFactory {
    private static final InputDescriptorStrategy INPUT_DESCRIPTOR_STRATEGY = new InputDescriptorStrategy();
    private static final OutputDescriptorStrategy OUTPUT_DESCRIPTOR_STRATEGY = new OutputDescriptorStrategy();

    private DescriptorFactory() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * Creates the {@link FieldDescriptor} for given {@code owner}, {@link Field} and {@code annotation}
     *
     * @param owner      the owner that holds the field; may not be null
     * @param field      the field that should be described; may not be null
     * @param annotation the type of annotation to define the {@link DescriptorStrategy}; may not be null
     * @return {@link Descriptor} instance
     * @throws IllegalArgumentException if no suitable {@link DescriptorStrategy} could be found
     */
    public static FieldDescriptor createFieldDescriptor(final Object owner,
                                                        final Field field,
                                                        final Class<? extends Annotation> annotation) {
        if (Input.class.isAssignableFrom(annotation)) {
            return INPUT_DESCRIPTOR_STRATEGY.createDescriptor(owner, field);
        } else if (Output.class.isAssignableFrom(annotation)) {
            return OUTPUT_DESCRIPTOR_STRATEGY.createDescriptor(owner, field);
        }
        throw new IllegalArgumentException(String.format("No suitable DescriptorStrategy found for given: owner=%s, field=%s and annotation=%s",
                owner, field, annotation));
    }
}
