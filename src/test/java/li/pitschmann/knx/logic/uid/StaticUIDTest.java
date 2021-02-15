package li.pitschmann.knx.logic.uid;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link StaticUID}
 */
public class StaticUIDTest {
    @Test
    @DisplayName("Test static UID")
    public void testBaseUID() {
        final var uidAsString = "STATIC-UID";
        final var staticUID = UIDFactory.createUid(uidAsString);
        final var staticUID2 = UIDFactory.createUid(uidAsString);

        // verify default methods
        assertThat(staticUID.equals(null)).isFalse();
        assertThat(staticUID.equals(staticUID)).isTrue();
        assertThat(staticUID.equals(staticUID2)).isTrue();
        assertThat(staticUID.hashCode()).isEqualTo(uidAsString.hashCode());
        assertThat(staticUID).hasToString(uidAsString);
    }

    @Test
    @DisplayName("Test static UID with NULL as uid string")
    public void testBaseUIDNullString() {
        assertThatThrownBy(() -> UIDFactory.createUid(null))
                .isInstanceOf(NullPointerException.class);
    }
}
