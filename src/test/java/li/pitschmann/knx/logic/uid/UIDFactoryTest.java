package li.pitschmann.knx.logic.uid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link UIDFactory}
 */
public class UIDFactoryTest {
    @Test
    @DisplayName("Test UID types")
    public void testUIDTypes() {
        // Static UID
        final var staticUid = UIDFactory.createUid("foobar");
        assertThat(staticUid).isInstanceOf(StaticUID.class).hasToString("foobar");

        // Static UID (randomized)
        final var staticRandomUid = UIDFactory.createRandomUid();
        assertThat(staticRandomUid).isInstanceOf(StaticUID.class);
        assertThat(staticRandomUid.toString()).matches("" +
                "^" + //
                "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}" + // from UUID
                "$" //
        );
    }

    @Test
    @DisplayName("Constructor not instantiable")
    public void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = UIDFactory.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }
}
