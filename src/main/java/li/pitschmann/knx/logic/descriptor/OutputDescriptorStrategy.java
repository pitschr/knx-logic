package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.logic.annotations.Output;

import java.lang.reflect.Field;

/**
 * {@link DescriptorStrategy} for {@link Output} fields
 *
 * @author PITSCHR
 */
public final class OutputDescriptorStrategy implements DescriptorStrategy {

    @Override
    public OutputDescriptor createDescriptor(final Object owner, final Field field) {
        final var annotation = field.getAnnotation(Output.class);
        return new OutputDescriptor(owner, annotation, field);
    }
}
