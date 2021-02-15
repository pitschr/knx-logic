package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.uid.UID;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Marker Interface for instances that are aware of pins (e.g. input, output)
 *
 * @author PITSCHR
 */
public interface PinAware {
    /**
     * Returns a list of all available {@link Pin}s
     *
     * @return list of {@link Pin}
     */
    List<Pin> getPins();

    /**
     * Returns {@link Pin} by for given name which has two different :
     * <ul>
     *     <li>If it is a static pin, then provide only name (Example: {@code input}).</li>
     *     <li>If it is a dynamic pin, then provide name and index using {@code name[index]} (Example: {@code inputs[2]}).</li>
     * </ul>
     *
     * @param fieldName the field name that should be look up for (provide with index number, if dynamic)
     * @return {@link Pin}
     * @throws NoSuchElementException if no suitable {@link Pin} was found
     */
    default Pin getPin(final String fieldName) {
        Preconditions.checkNonNull(fieldName);
        // is dynamic pin?
        if (fieldName.contains("[") && fieldName.endsWith("]")) {
            // look up for dynamic pin with given index
            final var name = fieldName.substring(0, fieldName.indexOf('['));
            final var index = Integer.parseInt(fieldName.substring(fieldName.indexOf('[') + 1, fieldName.length() - 1));

            return getPins()
                    .stream()
                    .filter(p -> name.equals(p.getDescriptor().getName()))
                    .filter(DynamicPin.class::isInstance)
                    .map(DynamicPin.class::cast)
                    .filter(p -> index == p.getIndex())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No Dynamic Pin with field name found: " + fieldName));
        } else {
            // it can only be a static pin
            return getPins()
                    .stream()
                    .filter(p -> fieldName.equals(p.getDescriptor().getName()))
                    .filter(StaticPin.class::isInstance)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No Static Pin with field name found: " + fieldName));
        }
    }

    /**
     * Returns any matching {@link Pin} by for given {@link UID}
     *
     * @param uid the UID that is used by the {@link Pin}
     * @return {@link Pin}
     * @throws NoSuchElementException if no suitable {@link Pin} was found
     */
    default Pin getPin(final UID uid) {
        return getPins()
                .stream()
                .filter(p -> p.getUid().equals(uid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Pin by UID found: " + uid));
    }
}
