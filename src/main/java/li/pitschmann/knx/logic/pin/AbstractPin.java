package li.pitschmann.knx.logic.pin;

import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.descriptor.InputDescriptor;
import li.pitschmann.knx.logic.descriptor.OutputDescriptor;
import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Abstract {@link Pin}
 *
 * @param <C> the type of connector
 * @author PITSCHR
 */
abstract class AbstractPin<C extends Connector> implements Pin {
    private final C connector;
    private final AtomicBoolean refresh = new AtomicBoolean();
    private final boolean alwaysTrigger;
    private UID uid = UIDFactory.createRandomUid();
    private long refreshCount = 0L;

    /**
     * Constructor for {@link AbstractPin}
     *
     * @param connector the connector that is responsible for the pin
     */
    protected AbstractPin(final C connector) {
        this.connector = Objects.requireNonNull(connector);
        // mark pin as refreshed (initially)
        setRefresh();

        // for input/output we may have a special logic
        final var descriptor = connector.getDescriptor();
        if (descriptor instanceof InputDescriptor) {
            this.alwaysTrigger = ((InputDescriptor) descriptor).isAlwaysTrigger();
        } else if (descriptor instanceof OutputDescriptor) {
            this.alwaysTrigger = ((OutputDescriptor) descriptor).isAlwaysTrigger();
        } else {
            // otherwise the alwaysTrigger is deactivated
            this.alwaysTrigger = false;
        }
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
    public final C getConnector() {
        return this.connector;
    }

    @Override
    public final boolean isRefresh() {
        return this.refresh.get();
    }

    @Override
    public boolean isAlwaysTrigger() {
        return alwaysTrigger;
    }

    @Override
    public final void setRefresh() {
        if (!this.refresh.getAndSet(true)) {
            refreshCount++;
        }
    }

    @Override
    public final void clearRefresh() {
        this.refresh.set(false);
    }

    @Override
    public long refreshCount() {
        return this.refreshCount;
    }
}
