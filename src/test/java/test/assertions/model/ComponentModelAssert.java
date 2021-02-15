package test.assertions.model;

import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.uid.UIDAware;

import static org.assertj.core.api.Assertions.assertThat;

public final class ComponentModelAssert extends ModelAssert<ComponentModelAssert, ComponentModel> {
    /**
     * Constructor for {@link ComponentModelAssert}
     *
     * @param actual
     */
    public ComponentModelAssert(final ComponentModel actual) {
        super(actual, ComponentModelAssert.class);
    }

    /**
     * Assert {@link ComponentModel#getUid()}
     *
     * @param expected
     * @return myself
     */
    public ComponentModelAssert uid(final UIDAware expected) {
        assertThat(this.actual.getUid()).isEqualTo(expected.getUid());
        return this.myself;
    }

    /**
     * Assert {@link ComponentModel#getClassName()}
     *
     * @param className
     * @return myself
     */
    public ComponentModelAssert className(final Class<?> className) {
        assertThat(this.actual.getClassName()).isEqualTo(className);
        return this.myself;
    }

    /**
     * Assert {@link ComponentModel#getComponentType()}
     *
     * @param componentType
     * @return myself
     */
    public ComponentModelAssert componentType(final ComponentType componentType) {
        assertThat(this.actual.getComponentType()).isSameAs(componentType);
        return this.myself;
    }
}
