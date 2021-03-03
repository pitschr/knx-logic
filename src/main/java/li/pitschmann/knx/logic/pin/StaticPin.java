package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.connector.StaticConnector;
import li.pitschmann.knx.logic.helpers.ReflectHelper;
import li.pitschmann.knx.logic.helpers.ValueHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Static Field with 1-to-1 relationship
 *
 * @author PITSCHR
 */
public final class StaticPin extends AbstractPin<StaticConnector> {
    private static final Logger LOG = LoggerFactory.getLogger(StaticPin.class);

    /**
     * Constructor for {@link StaticPin}
     *
     * @param connector the static connector owning this static pin
     */
    public StaticPin(final StaticConnector connector) {
        super(connector);

        tryInitializeDefaultValue();
    }

    /**
     * Tries to initialize the default value in case no value
     * was set.
     */
    private void tryInitializeDefaultValue() {
        final var descriptor = getDescriptor();
        final var obj = ReflectHelper.getInternalValue(descriptor.getOwner(), descriptor.getField());
        if (obj == null) {
            final var defaultValue = ValueHelper.getDefaultValueFor(descriptor.getFieldType());
            ReflectHelper.setInternalValue(descriptor.getOwner(), descriptor.getField(), defaultValue);
            LOG.debug("Static Pin '{}' initialized with default value: {}", getUid(), defaultValue);
        } else {
            LOG.debug("Static Pin '{}' initialized with value: {}", getUid(), obj);
        }
    }

    @Override
    @Nullable
    public Object getValue() {
        return ReflectHelper.getInternalValue(getDescriptor().getOwner(), getDescriptor().getField());
    }

    @Override
    public void setValue(final @Nullable Object newValue) {
        final var oldValue = this.getValue();

        final var valueChanged = !Objects.equals(oldValue, newValue);
        if (valueChanged || isAlwaysTrigger()) {
            if (newValue == null) {
                ReflectHelper.setInternalValue(getDescriptor().getOwner(), getDescriptor().getField(), ValueHelper.getDefaultValueFor(getDescriptor().getFieldType()));
            } else {
                ReflectHelper.setInternalValue(getDescriptor().getOwner(), getDescriptor().getField(), newValue);
            }
            this.setRefresh();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Value set for '{}' (old: {}, new: {}, valueChanged: {})",
                    getUid(), oldValue, newValue, valueChanged);
        }
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("uid", getUid()) //
                .add("fieldName", getDescriptor().getName()) //
                .add("fieldType", getDescriptor().getFieldType().getName()) //
                .add("value", getValue()) //
                .add("refresh", isRefresh()) //
                .toString();
    }
}
