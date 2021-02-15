package li.pitschmann.knx.logic.descriptor;

import java.lang.reflect.Field;

/**
 * Strategy for creating a new {@link Descriptor}.
 *
 * @author PITSCHR
 */
public interface DescriptorStrategy {
    /**
     * Creates a new instance of {@link Descriptor}
     *
     * @param owner the owner of descriptor; may not be null
     * @param field the field that is described by descriptor; may not be null
     * @return a new instance of {@link Descriptor}
     */
    Descriptor createDescriptor(Object owner, Field field);
}
