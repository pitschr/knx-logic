package li.pitschmann.knx.logic.connector;

import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;

import java.util.Objects;

/**
 * Abstract {@link Connector}
 * <p/>
 * This class provides the default implementation for all {@link Connector}
 * interface. All {@link Connector} is associated to a field that is owned
 * by an instance and provides a field descriptor.
 *
 * @author PITSCHR
 */
public abstract class AbstractConnector implements Connector {
    private final FieldDescriptor descriptor;
    private UID uid = UIDFactory.createRandomUid();

    /**
     * Constructor for {@link AbstractConnector} that implements
     * basic functionality for {@link Connector}
     *
     * @param descriptor descriptor of field that is wrapped by the connector
     */
    protected AbstractConnector(final FieldDescriptor descriptor) {
        this.descriptor = Objects.requireNonNull(descriptor);
    }

    @Override
    public final UID getUid() {
        return this.uid;
    }

    /**
     * Overrides the {@link UID} which was created as random
     *
     * @param uid the {@link UID} to be used; may not be null
     */
    public final void setUid(final UID uid) {
        this.uid = Objects.requireNonNull(uid);
    }

    @Override
    public final FieldDescriptor getDescriptor() {
        return this.descriptor;
    }
}
