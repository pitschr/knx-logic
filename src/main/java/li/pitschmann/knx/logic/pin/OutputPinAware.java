package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.logic.uid.UID;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Interface that current component is aware about output {@link Pin} fields
 *
 * @author PITSCHR
 */
public interface OutputPinAware extends PinAware {
    /**
     * Returns a list of {@link Pin}
     *
     * @return list of {@link Pin}
     */
    List<Pin> getOutputPins();

    /**
     * Returns {@link Pin} for given {@code index}
     *
     * @param index
     * @return {@link Pin} which is an instance of {@code <T>}
     */
    default Pin getOutputPin(int index) {
        return getOutputPins().get(index);
    }

    /**
     * Returns output {@link Pin} by for given name which has two different :
     * <ul>
     *     <li>If it is a static pin, then provide only name (Example: {@code output}).</li>
     *     <li>If it is a dynamic pin, then provide name and index using {@code name[index]} (Example: {@code output[3]}).</li>
     * </ul>
     *
     * @param fieldName the field name that should be look up for (provide with index number, if dynamic)
     * @return output {@link Pin}
     */
    default Pin getOutputPin(final String fieldName) {
        Preconditions.checkNonNull(fieldName);
        // is dynamic pin?
        if (fieldName.contains("[") && fieldName.endsWith("]")) {
            // look up for dynamic pin with given index
            final var name = fieldName.substring(0, fieldName.indexOf('['));
            final var index = Integer.parseInt(fieldName.substring(fieldName.indexOf('[') + 1, fieldName.length() - 1));

            return getOutputPins()
                    .stream()
                    .filter(p -> name.equals(p.getDescriptor().getName()))
                    .filter(DynamicPin.class::isInstance)
                    .map(DynamicPin.class::cast)
                    .filter(p -> index == p.getIndex())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No Output Dynamic Pin with field name found: " + fieldName));
        } else {
            // it can only be a static pin
            return getOutputPins()
                    .stream()
                    .filter(p -> fieldName.equals(p.getDescriptor().getName()))
                    .filter(StaticPin.class::isInstance)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No Output Static Pin with field name found: " + fieldName));
        }
    }

    /**
     * Returns output {@link Pin} by for given {@link UID}
     *
     * @param uid the UID that is used by the {@link Pin}
     * @return output {@link Pin}
     * @throws NoSuchElementException if no suitable {@link Pin} was found
     */
    default Pin getOutputPin(final UID uid) {
        return getOutputPins()
                .stream()
                .filter(p -> p.getUid().equals(uid))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No Output Pin by UID found: " + uid));
    }
}
