package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.StaticPin;

import java.util.stream.Stream;

/**
 * Connector for {@link StaticPin}
 *
 * @author PITSCHR
 */
public final class StaticConnector extends AbstractConnector
        implements StaticConnectorAware {
    private final StaticPin pin;

    /**
     * Constructor for {@link StaticPin} that represents a
     * single field.
     *
     * @param descriptor field descriptor; may not be null
     */
    public StaticConnector(final FieldDescriptor descriptor) {
        super(descriptor);

        this.pin = new StaticPin(this);
    }

    @Override
    public Stream<Pin> getPinStream() {
        return Stream.of(this.pin);
    }

    @Override
    public StaticPin getPin() {
        return this.pin;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("descriptor", getDescriptor()) //
                .add("pin", this.pin) //
                .toString();
    }
}
