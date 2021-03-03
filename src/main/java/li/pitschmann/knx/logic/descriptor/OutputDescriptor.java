package li.pitschmann.knx.logic.descriptor;

import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.Trigger;
import li.pitschmann.knx.logic.annotations.Output;

import java.lang.reflect.Field;

/**
 * Descriptor for {@link Output} annotated fields
 */
public final class OutputDescriptor extends FieldDescriptor {
    private final int min;
    private final int max;
    private final boolean alwaysTrigger;

    OutputDescriptor(final Object owner, final Output annotation, final Field field) {
        super(owner, field);

        final var min = annotation.min();
        final var max = annotation.max();
        final var trigger = annotation.trigger();
        Preconditions.checkArgument(min >= 0, "Value for 'min' must be zero/positive!");
        Preconditions.checkArgument(max >= 0, "Value for 'max' must be zero/positive!");
        Preconditions.checkArgument(max >= min,
                "Value for 'max' may not be smaller than 'min': min={}, max={}", min, max);
        Preconditions.checkNonNull(trigger, "Trigger may not be null!");
        this.min = min;
        this.max = max;
        alwaysTrigger = trigger == Trigger.ALWAYS;
    }

    /**
     * <p>Minimum of occurrences for output values.</p>
     *
     * @return number of minimum occurrences. The number is positive.
     */
    public int getMin() {
        return min;
    }

    /**
     * <p>Maximum of occurrences for output values.</p>
     *
     * @return number of maximum occurrences. The number is positive and is not smaller than number from {@link #getMin()}
     */
    public int getMax() {
        return max;
    }

    /**
     * <p>If the pin should be always triggered</p>
     *
     * @return {@code true} if always triggered, {@code false} otherwise
     */
    public boolean isAlwaysTrigger() {
        return alwaysTrigger;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("owner", getOwner()) //
                .add("name", getName()) //
                .add("field", getField()) //
                .add("fieldType", getFieldType().getName()) //
                .add("min", min) //
                .add("max", max) //
                .add("alwaysTrigger", alwaysTrigger) //
                .toString();
    }
}
