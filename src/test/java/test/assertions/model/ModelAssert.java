package test.assertions.model;

import li.pitschmann.knx.logic.db.models.Model;
import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelAssert<S extends AbstractAssert<S, A>, A extends Model> extends AbstractAssert<S, A> {
    /**
     * Constructor for {@link ModelAssert}
     *
     * @param actual
     */
    public ModelAssert(final A actual, final Class<S> selfType) {
        super(actual, selfType);
    }

    /**
     * Assert {@link Model#getId()}
     *
     * @param expected
     * @return myself
     */
    public S id(final int expected) {
        assertThat(this.actual.getId()).isEqualTo(expected);
        return this.myself;
    }
}
