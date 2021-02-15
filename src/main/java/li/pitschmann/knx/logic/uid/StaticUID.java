package li.pitschmann.knx.logic.uid;

import li.pitschmann.knx.core.annotations.Nullable;

import java.util.Objects;

/**
 * Static {@link UID} class. This class is immutable.
 *
 * @author PITSCHR
 */
public final class StaticUID implements UID {
    private final String uid;

    /**
     * Package-protected constructor for {@link StaticUID}.
     * This class should be instantiated by {@link UIDFactory}
     *
     * @param uid the string representation of {@link UID}
     */
    StaticUID(final String uid) {
        this.uid = Objects.requireNonNull(uid);
    }

    @Override
    public int hashCode() {
        return this.uid.hashCode();
    }

    @Override
    public boolean equals(final @Nullable Object obj) {
        if (obj instanceof StaticUID) {
            return Objects.equals(uid, ((StaticUID) obj).uid);
        }
        return false;
    }

    @Override
    public String toString() {
        return uid;
    }
}
