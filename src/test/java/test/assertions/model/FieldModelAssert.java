package test.assertions.model;

import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.uid.UIDAware;

import static org.assertj.core.api.Assertions.assertThat;

public final class FieldModelAssert extends ModelAssert<FieldModelAssert, PinModel> {
    /**
     * Constructor for {@link PinModel}
     *
     * @param actual
     */
    public FieldModelAssert(final PinModel actual) {
        super(actual, FieldModelAssert.class);
    }

    /**
     * Assert {@link PinModel#getConnectorId()}
     *
     * @param expected
     * @return myself
     */
    public FieldModelAssert connectorId(final int expected) {
        assertThat(this.actual.getConnectorId()).isEqualTo(expected);
        return this.myself;
    }

    /**
     * Assert {@link PinModel#getUid()}
     *
     * @param expected
     * @return myself
     */
    public FieldModelAssert uid(final UIDAware expected) {
        assertThat(this.actual.getUid()).isEqualTo(expected.getUid());
        return this.myself;
    }

    /**
     * Assert {@link PinModel#getIndex()}
     *
     * @param expected
     * @return myself
     */
    public FieldModelAssert index(final int expected) {
        assertThat(this.actual.getIndex()).isEqualTo(expected);
        return this.myself;
    }

}
