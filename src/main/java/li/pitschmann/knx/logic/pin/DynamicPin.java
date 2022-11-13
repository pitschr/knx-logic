package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.core.annotations.Nullable;
import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.connector.DynamicConnector;
import li.pitschmann.knx.logic.helpers.ReflectHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * Dynamic pin with 1-to-N relationship
 *
 * @author PITSCHR
 */
public final class DynamicPin extends AbstractPin<DynamicConnector> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamicPin.class);
    private final int index;
    private final List<Object> list;

    /**
     * Constructor for {@link DynamicPin}
     *
     * @param connector the dynamic connector owning this pin
     * @param index     the index of dynamic pin that is owned by connector
     */
    public DynamicPin(final DynamicConnector connector, final int index) {
        super(connector);
        this.index = index;
        list = ReflectHelper.getInternalValue(connector.getDescriptor().getOwner(), connector.getDescriptor().getField());
    }

    /**
     * Returns the index of {@link DynamicPin}. The index may be subject
     * to be changed when the list of {@link DynamicPin} is altered.
     *
     * @return current index
     */
    public int getIndex() {
        return this.index;
    }

    @Override
    @Nullable
    public Object getValue() {
        return this.list.get(this.index);
    }

    @Override
    public void setValue(final @Nullable Object newValue) {
        final var oldValue = getValue();

        final var valueChanged = !Objects.equals(oldValue, newValue);
        if (valueChanged || isAlwaysTrigger()) {
            list.set(this.index, newValue);
            setRefresh();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Value set for '{}' (old: {}, new: {}, valueChanged: {})",
                    getUid(), oldValue, newValue, valueChanged);
        }
    }

    @Override
    public String getName() {
        return getDescriptor().getField().getName() + "[" + getIndex() + "]";
    }

    @Override
    public String toString() {
        final var fieldName = String.format("%s[%s]", getDescriptor().getName(), getIndex());

        return Strings.toStringHelper(this) //
                .add("uid", getUid()) //
                .add("fieldName", fieldName) //
                .add("fieldType", getDescriptor().getFieldType().getName()) //
                .add("value", getValue()) //
                .add("refresh", isRefresh()) //
                .toString();
    }
}
