package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.ByteFormatter;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.uid.UID;

import java.time.Instant;
import java.util.Objects;

/**
 * Immutable workflow entry containing:
 * <ul>
 *     <li>Instant for timestamp; the instantiation timestamp is used</li>
 *     <li>UID for identification to whom the value belongs to at the timestamp</li>
 *     <li>the value; may be null</li>
 * </ul>
 *
 * @author PITSCHR
 */
public final class WorkflowEntry {
    private final Instant instant;
    private final UID uid;
    private final Object value;

    public WorkflowEntry(final UID uid, final @Nullable Object value) {
        this.instant = Instant.now();
        this.uid = Objects.requireNonNull(uid);
        this.value = value;
    }

    /**
     * The timestamp when the workflow entry was created
     *
     * @return the {@link Instant}
     */
    public Instant getInstant() {
        return instant;
    }

    /**
     * The {@link UID} of component for the workflow entry
     *
     * @return the UID
     */
    public UID getUid() {
        return uid;
    }

    /**
     * The value of workflow entry when it was hold by the {@link UID} component
     * at the defined {@link Instant}.
     *
     * @return the value; may be null
     */
    @Nullable
    public Object getValue() {
        return value;
    }

    /**
     * Returns a string representation of the {@link WorkflowEntry}.
     * <p>
     * If the value is a byte-array then it is printed as a hex-string, otherwise
     * the native string representation of the value is returned.
     *
     * @return a string representation of the {@link WorkflowEntry}
     */
    @Override
    public String toString() {
        // get human-friendly string for byte array
        final String valueAsString;
        if (value != null && value.getClass().isArray() && value.getClass().getComponentType().equals(byte.class)) {
            valueAsString = ByteFormatter.formatHexAsString((byte[]) value);
        } else {
            valueAsString = String.valueOf(value);
        }

        return Strings.toStringHelper(this) //
                .add("instant", instant) //
                .add("uid", uid) //
                .add("value", valueAsString) //
                .toString();
    }
}
