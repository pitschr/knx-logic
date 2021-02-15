package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.descriptor.DescriptorFactory;
import li.pitschmann.knx.logic.helpers.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ConnectorStrategy} for fetching all {@link Connector} based on the fields
 * with annotations described by {@link #getAnnotationClass()}.
 *
 * @author PITSCHR
 */
public abstract class AbstractConnectorStrategy implements ConnectorStrategy {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Returns the class of annotation that should be used for look up
     *
     * @return class of annotation; may not be null
     */
    protected abstract Class<? extends Annotation> getAnnotationClass();

    /**
     * <p>Returns an unmodifiable list of connectors that extends type of {@link #getAnnotationClass()}.</p>
     * <p>The the concrete implementation of {@link Connector} is based on the type of the
     * {@link Field}. If the field type is Iterable then the {@link Connector} is a {@link DynamicConnector},
     * otherwise it is a {@link StaticConnector}</p>
     *
     * @param obj the object that contains connectors; may not be null
     * @return list of {@link Connector}; is empty if no suitable connector found
     */
    @Override
    public List<Connector> getConnectors(final Object obj) {
        // Get annotated fields (including fields from super classes)
        final var annotationClass = getAnnotationClass();
        final var fields = ReflectHelper.filterFieldsByAnnotation(obj.getClass().getDeclaredFields(), annotationClass);

        // Return unmodifiable empty list if no annotation specific field is available
        if (fields.isEmpty()) {
            return List.of();
        }

        final var connectors = new ArrayList<Connector>(fields.size());
        for (final var field : fields) {
            final var descriptor = DescriptorFactory.createFieldDescriptor(obj, field, annotationClass);
            final Connector connector;
            if (Iterable.class.isAssignableFrom(field.getType())) {
                connector = new DynamicConnector(descriptor);
            } else {
                connector = new StaticConnector(descriptor);
            }
            connectors.add(connector);
        }

        log.debug("Connectors for annotation '{}': {}", annotationClass, connectors);
        return List.copyOf(connectors);
    }

}
