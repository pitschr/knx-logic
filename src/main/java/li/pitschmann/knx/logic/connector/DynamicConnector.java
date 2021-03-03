package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Preconditions;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.descriptor.InputDescriptor;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
import li.pitschmann.knx.logic.helpers.ReflectHelper;
import li.pitschmann.knx.logic.helpers.ValueHelper;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Dynamic Connector for {@link DynamicPin}s
 * <p>
 * Using dynamic connector we have the flexibility to provide multiple {@link Pin}
 * per field. In case the field is an array, the size of dynamic connector is fixed.
 * If the field is a type of {@link Iterable} then the size of connector may vary;
 * depending on the setting of annotation.
 * <pre>{@code
 * final var connector = (DynamicConnector)logic.getInputConnector("dynamicField");
 * final var 2ndPin = connector.getPin(2);  // return the pin on index = 2
 * final var newPin = connector.addPin();   // add a new pin at the last index
 * }</pre>
 *
 * @author PITSCHR
 */
public final class DynamicConnector extends AbstractConnector
        implements DynamicConnectorAware {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicConnector.class);
    private static final boolean FIELD_SYNC_DEEP_CHECK = !false; // true = performs a deep sync (performance!)
    private static final int FIELD_INITIAL_SIZE = 16; // hopefully big enough for most cases!

    private final AtomicReference<List<Object>> dynamicListReference = new AtomicReference<>();
    private final List<DynamicPin> pins = new ArrayList<>(FIELD_INITIAL_SIZE);
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = this.readWriteLock.readLock();
    private final Lock writeLock = this.readWriteLock.writeLock();
    private final Object defaultValue;

    /**
     * Constructor for {@link DynamicConnector}
     *
     * @param descriptor the field descriptor of dynamic field instance; may not be null
     */
    public DynamicConnector(final FieldDescriptor descriptor) {
        super(descriptor);

        // set the default value for each (new) dynamic pins
        defaultValue = ValueHelper.getDefaultValueFor(descriptor.getFieldType());

        initializeDynamicPins(descriptor);
        integrityDynamicPinCheck();
    }

    @Override
    public Stream<Pin> getPinStream() {
        return this.getPins().stream().map(Pin.class::cast);
    }

    @Override
    public int size() {
        return this.getPins().size();
    }

    /**
     * Internal method that is called during initialization
     * to create dynamic pins. This method is designed to be
     * called on initialization only.
     */
    private void initializeDynamicPins(final FieldDescriptor descriptor) {
        // create a copy of original list and retrieve its size
        final var originalIterable = ReflectHelper.getInternalValue(descriptor.getOwner(), descriptor.getField());
        final var originalList = new ArrayList<>();
        if (originalIterable instanceof Collection) {
            originalList.addAll((Collection<?>) originalIterable);
        } else if (originalIterable instanceof Iterable) {
            ((Iterable<?>) originalIterable).forEach(originalList::add);
        }

        // create a new list and increase to the minimum occurrence
        // all values are taken from defaultValue
        final var list = new ArrayList<>(FIELD_INITIAL_SIZE);
        ReflectHelper.setInternalValue(descriptor.getOwner(), descriptor.getField(), list);
        dynamicListReference.set(list);
        tryIncrease(Math.max(originalList.size(), minimumOfOccurrences()));

        // check if the original list is already allocated.
        // If it contains defined values then move the values to the new list
        if (!originalList.isEmpty()) {
            for (var i = 0; i < originalList.size(); i++) {
                list.set(i, originalList.get(i));
            }
            LOG.debug("Dynamic pins for connector '{}' were already initialized. Re-Initialized with array list: {}", getName(), list);
        }
        LOG.debug("Dynamic pins for connector '{}' are: {}", getName(), list);
    }

    @Override
    public List<DynamicPin> getPins() {
        this.readLock.lock();
        try {
            return List.copyOf(this.pins);
        } finally {
            integrityDynamicPinCheck();
            this.readLock.unlock();
        }
    }

    @Override
    @Nullable
    public DynamicPin getPin(final int index) {
        this.readLock.lock();
        try {
            return this.pins.get(index);
        } finally {
            integrityDynamicPinCheck();
            this.readLock.unlock();
        }
    }

    @Override
    public DynamicPin addPin() {
        return addPin(size());
    }

    @Override
    public DynamicPin addPin(final int index) {
        this.writeLock.lock();
        try {
            if (size() >= maximumOfOccurrences()) {
                throw new ArrayIndexOutOfBoundsException(
                        String.format(
                                "Maximum of occurrences already reached for connector '%s': desired=%s, actual=%s",
                                getName(),
                                maximumOfOccurrences(),
                                size()
                        )
                );
            }

            this.dynamicListReference.get().add(index, defaultValue);

            // create new
            final var newPin = new DynamicPin(this, index);
            this.pins.add(index, newPin);

            // verify&correct index
            this.correctIndex(this.pins);

            if (LOG.isDebugEnabled()) {
                LOG.debug("New dynamic pin added: {}[{}] (owner={})", getDescriptor().getName(), index, getDescriptor().getOwner());
            }
            return newPin;
        } finally {
            integrityDynamicPinCheck();
            this.writeLock.unlock();
        }
    }

    @Override
    public List<DynamicPin> tryIncrease(final int desiredSize) {
        this.writeLock.lock();
        try {
            // desired size to be corrected?
            final var actualSize = size();
            if (actualSize >= desiredSize) {
                LOG.debug("Desired size already reached: actual={}, desiredSize={}", actualSize, desiredSize);
                return List.of();
            } else {
                final var delta = Math.min(desiredSize, maximumOfOccurrences()) - actualSize;
                final var tmpPins = new ArrayList<DynamicPin>(delta);
                for (int i = 0; i < delta; i++) {
                    tmpPins.add(addPin());
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("{} new dynamic pin added for: {}", tmpPins.size(), getDescriptor().getOwner());
                }
                return List.copyOf(tmpPins);
            }
        } finally {
            integrityDynamicPinCheck();
            this.writeLock.unlock();
        }
    }

    @Override
    public DynamicPin removePin(final int index) {
        this.writeLock.lock();
        try {
            if (size() <= minimumOfOccurrences()) {
                throw new ArrayIndexOutOfBoundsException(
                        String.format(
                                "Minimum of occurrences already reached for connector '%s': desired=%s, actual=%s",
                                getName(),
                                minimumOfOccurrences(),
                                size()
                        )
                );
            }

            this.dynamicListReference.get().remove(index);
            final var oldPin = this.pins.remove(index);
            LOG.debug("Dynamic pin removed from list index {}: {}", index, oldPin.getUid());

            // verify&correct index
            this.correctIndex(this.pins);

            return oldPin;
        } finally {
            integrityDynamicPinCheck();
            this.writeLock.unlock();
        }
    }

    @Override
    public void reset() {
        this.writeLock.lock();
        try {
            this.dynamicListReference.get().clear();
            this.pins.clear();

            tryIncrease(minimumOfOccurrences());
        } finally {
            integrityDynamicPinCheck();
            this.writeLock.unlock();
        }
    }

    private int maximumOfOccurrences() {
        final var descriptor = getDescriptor();
        if (descriptor instanceof InputDescriptor) {
            return ((InputDescriptor) descriptor).getMax();
        } else if (descriptor instanceof OutputDescriptor) {
            return ((OutputDescriptor) descriptor).getMax();
        }
        return Integer.MAX_VALUE;
    }

    private int minimumOfOccurrences() {
        final var descriptor = getDescriptor();
        if (descriptor instanceof InputDescriptor) {
            return ((InputDescriptor) descriptor).getMin();
        } else if (descriptor instanceof OutputDescriptor) {
            return ((OutputDescriptor) descriptor).getMin();
        }
        return 0;
    }

    /**
     * Corrects the index of {@link DynamicPin} in the given {@code pins} list.
     * This method must be called when adding or removing a {@link DynamicPin}.
     *
     * @param pins list of dynamic pins that may be subject to be corrected; may not be null
     */
    private void correctIndex(final List<DynamicPin> pins) {
        var corrected = false;
        for (var i = 0; i < pins.size(); i++) {
            final var pin = pins.get(i);
            if (pin.getIndex() != i) {
                LOG.debug("Correct index for pin '{}' (before: {}, after: {})", pin.getUid(), pin.getIndex(), i);
                ReflectHelper.setInternalValue(pin, "index", i);
                corrected = true;
            }
        }

        if (corrected) {
            LOG.debug("Index order of 'internalPins' now: {}", this.pins);
        }
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("fieldName", getDescriptor().getName()) //
                .add("fieldType", getDescriptor().getFieldType().getName()) //
                .add("defaultValue", defaultValue) //
                .add("pins", pins.stream().map(DynamicPin::getUid).collect(Collectors.toList())) //
                .toString();
    }

    /**
     * Checks the integrity of dynamic pins (that is owned by wrapper) and
     * dynamic reference list (that is owned by logic component)
     */
    private void integrityDynamicPinCheck() {
        this.readLock.lock();
        try {
            final var checkPins = List.copyOf(this.pins);
            final var checkDynamicListValues = new ArrayList<>(this.dynamicListReference.get()); // as it may contain null values

            // verify if the size of internalPins and dynamicPinReferences matches
            Preconditions.checkState(checkDynamicListValues.size() == checkPins.size(),
                    "Size of 'dynamicListReference' and 'pins' doesn't match! (dynamicListReference: {}, pins: {})",
                    checkDynamicListValues.size(), checkPins.size());

            // deep check (value check, index check)
            if (FIELD_SYNC_DEEP_CHECK) {
                for (var i = 0; i < checkPins.size(); i++) {
                    final var internalVal = checkPins.get(i).getValue();
                    final var referenceVal = checkDynamicListValues.get(i);

                    Preconditions.checkState(checkPins.get(i).getIndex() == i,
                            "Index of 'pins' doesn't match with current index '{}'! (pins.index: {})", i,
                            checkPins.get(i).getIndex());

                    Preconditions.checkState(Objects.equals(internalVal, referenceVal),
                            "Value of 'dynamicListReference' and 'pins' doesn't match! (dynamicListReference[{}]: {}, pins[{}]: {})",
                            i, referenceVal, i, internalVal);
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }
}
