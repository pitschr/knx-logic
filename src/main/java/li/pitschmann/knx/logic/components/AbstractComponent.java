package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.logic.uid.UID;
import li.pitschmann.knx.logic.uid.UIDFactory;

import java.util.Objects;

/**
 * Abstract Component wrapping an object
 *
 * @param <T> type of object to be wrapped by the component
 * @author PITSCHR
 */
abstract class AbstractComponent<T> implements Component {
    private final T wrappedObject;
    private UID uid = UIDFactory.createRandomUid();

    /**
     * Package-protected constructor that wraps the object
     *
     * @param object to be wrapped; may not be null
     */
    AbstractComponent(final T object) {
        this.wrappedObject = Objects.requireNonNull(object);
    }

    @Override
    public final UID getUid() {
        return uid;
    }

    /**
     * Overrides the {@link UID} which was created as random
     *
     * @param uid the {@link UID} to be used; may not be null
     */
    public final void setUid(final UID uid) {
        this.uid = uid;
    }

    @Override
    public T getWrappedObject() {
        return wrappedObject;
    }
}
