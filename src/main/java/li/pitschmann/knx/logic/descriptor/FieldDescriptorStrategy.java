package li.pitschmann.knx.logic.descriptor;

import java.lang.reflect.Field;

/**
 * Strategy for creating a new {@link FieldDescriptor}.
 *
 * @author PITSCHR
 */
public interface FieldDescriptorStrategy extends DescriptorStrategy {
    /**
     * Creates a new instance of {@link FieldDescriptor}
     *
     * @param owner the owner that holds the field descriptor; may not be null
     * @param field the field that describes the field descriptor; may not be null
     * @return {@link FieldDescriptor}
     */
    @Override
    FieldDescriptor createDescriptor(Object owner, Field field);
}
