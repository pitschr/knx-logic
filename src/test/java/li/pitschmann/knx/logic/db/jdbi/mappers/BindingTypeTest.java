package li.pitschmann.knx.logic.db.jdbi.mappers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link BindingType}
 */
public class BindingTypeTest {

    @Test
    @DisplayName("Test BindingType#valueOf(..)")
    public void testValueOf() {
        assertThat(BindingType.valueOf(0)).isSameAs(BindingType.STATIC);
        assertThat(BindingType.valueOf(1)).isSameAs(BindingType.DYNAMIC);

        assertThatThrownBy(() -> BindingType.valueOf(-1)).isInstanceOf(IllegalArgumentException.class);
    }
}
