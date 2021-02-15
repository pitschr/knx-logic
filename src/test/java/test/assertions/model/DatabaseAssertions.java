package test.assertions.model;

import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.db.models.PinModel;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.CheckReturnValue;

public class DatabaseAssertions extends Assertions {
    /**
     * Create assertion for {@link ComponentModel}.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public static ComponentModelAssert assertThat(final ComponentModel actual) {
        return new ComponentModelAssert(actual);
    }

    /**
     * Create assertion for {@link ConnectorModel}.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public static ConnectorModelAssert assertThat(final ConnectorModel actual) {
        return new ConnectorModelAssert(actual);
    }

    /**
     * Create assertion for {@link PinModel}.
     *
     * @param actual the actual value.
     * @return the created assertion object.
     */
    @CheckReturnValue
    public static FieldModelAssert assertThat(final PinModel actual) {
        return new FieldModelAssert(actual);
    }
}
