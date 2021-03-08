package li.pitschmann.knx.logic.uid;

import java.util.UUID;

/**
 * {@link UID} Factory for creating immutable UID implementations.
 *
 * @author PITSCHR
 */
public final class UIDFactory {
    private UIDFactory() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * Creates a static {@link UID} with a fixed string
     *
     * @param uid fixed uid string
     * @return {@link StaticUID}
     */
    public static StaticUID createUid(final String uid) {
        return new StaticUID(uid);
    }

    /**
     * Creates a randomized static {@link UID} with format
     * {@code UUID-hex(currentTimeMillis)}.
     * <p>
     * Example: {@code 5a5d08a4-21f1-4a88-8c81-f06d28480bd1}
     *
     * @return randomized {@link StaticUID}
     */
    public static StaticUID createRandomUid() {
        return new StaticUID(UUID.randomUUID().toString());
    }
}
