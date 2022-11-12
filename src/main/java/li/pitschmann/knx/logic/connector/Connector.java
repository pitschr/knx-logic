package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.uid.UIDAware;

import java.util.stream.Stream;

/**
 * <p>The {@link Connector} interface provides common methods
 * that are available for all type of connectors.</p>
 *
 * <pre>{@code
 * final var component = new LogicComponentImpl(
 *                             new Logic() {
 *                                 @Input
 *                                 private int myField;
 *                                 ...
 *                             }
 *                       );
 * final var connector = component.getInputConnector(0);
 * final var nameOfField = connector.getDescriptor().getName(); // output: 'myField'
 * }</pre>
 *
 * @author PITSCHR
 */
public interface Connector extends UIDAware {
    /**
     * Returns {@link Stream} of fields which are instance of {@link Pin}.
     * <p>In case the {@link Connector} is static then stream will always have
     * a single {@link Pin}</p>
     * <p>In case the {@link Connector} is dynamic then stream may contain
     * zero, one or many {@link Pin}s</p>
     *
     * @return {@link Stream} with type of {@link Pin}
     */
    Stream<Pin> getPinStream();

    /**
     * Gets the {@link FieldDescriptor} which contains some description
     * about the fields that is wrapped by the {@link Connector}.
     *
     * @return returns the {@link FieldDescriptor} of Connector
     */
    FieldDescriptor getDescriptor();

    /**
     * Checks if given {@code compatibleClass} is compatible with current connector
     *
     * @param compatibleClass the target class to be checked; may not be null
     * @return {@code true} if compatible, otherwise {@code false}
     */
    default boolean isCompatibleWith(final Class<?> compatibleClass) {
        return getDescriptor().getFieldType().isAssignableFrom(compatibleClass);
    }

    /**
     * Returns the name of {@link Connector} in following pattern:
     * <pre>{@code
     *      inputField
     * }</pre>
     *
     * @return name of Connector
     */
    default String getName() {
        return getDescriptor().getName();
    }

    /**
     * Returns the absolute name of {@link Connector} in following pattern:
     * <pre>{@code
     *      my.package.Logic#inputField
     * }</pre>
     *
     * @return name of Connector
     */
    default String getAbsoluteName() {
        return getDescriptor().getOwner().getClass().getName() + "#" + getName();
    }
}
