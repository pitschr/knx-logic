package test.assertions.model;

import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.models.ConnectorModel;

import static org.assertj.core.api.Assertions.assertThat;

public final class ConnectorModelAssert extends ModelAssert<ConnectorModelAssert, ConnectorModel> {
    /**
     * Constructor for {@link ConnectorModel}
     *
     * @param actual
     */
    public ConnectorModelAssert(final ConnectorModel actual) {
        super(actual, ConnectorModelAssert.class);
    }

    /**
     * Assert {@link ConnectorModel#getComponentId()}
     *
     * @param expected
     * @return myself
     */
    public ConnectorModelAssert componentId(final int expected) {
        assertThat(this.actual.getComponentId()).isEqualTo(expected);
        return this.myself;
    }

    /**
     * Assert {@link ConnectorModel#getBindingType()}
     *
     * @param expected
     * @return myself
     */
    public ConnectorModelAssert bindingType(final BindingType expected) {
        assertThat(this.actual.getBindingType()).isEqualTo(expected);
        return this.myself;
    }

    /**
     * Assert {@link ConnectorModel#getConnectorName()}
     *
     * @param expected
     * @return myself
     */
    public ConnectorModelAssert connectorName(final String expected) {
        assertThat(this.actual.getConnectorName()).isEqualTo(expected);
        return this.myself;
    }
}
