package li.pitschmann.knx.logic.db.strategies;

import li.pitschmann.knx.logic.components.outbox.VariableOutbox;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.TestHelpers;

import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing outbox components to database
 * {@link OutboxComponentPersistenceStrategy}
 *
 * @author PITSCHR
 */
class OutboxComponentPersistenceStrategyTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test the INSERT persistence of Outbox Component")
    void testInsert() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();

        final var outbox = TestHelpers.createOutboxComponent("outboxInsertKey");
        save(outbox);

        // ---------------------------------
        // Verification: Table Size
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);

        // ---------------------------------
        // Verification: Component
        // ---------------------------------
        final var componentModel = componentDao.getById(1);
        assertThat(componentModel)
                .componentType(ComponentType.OUTBOX)
                .className(VariableOutbox.class);
        assertThat(componentModel.getUid()).isNotNull();

        // ---------------------------------
        // Verification: Connectors
        // ---------------------------------
        final var connectorTexts = connectorsDao.getById(1);
        assertThat(connectorTexts)
                .id(1)
                .componentId(1)
                .bindingType(BindingType.STATIC)
                .connectorName("data");
        assertThat(pinsDao.getByConnectorId(1)).hasSize(1);

        // ---------------------------------
        // Verification: Pins
        // ---------------------------------
        final var pin = pinsDao.getById(1);
        assertThat(pin)
                .id(1)
                .connectorId(1)
                .index(0);
        assertThat(pin.getUid()).isNotNull();

        assertThat(pinsDao.getLastValueById(1)).isNull();

        // ---------------------------------
        // Verification: EventKey
        // ---------------------------------
        final var eventKey = eventKeyDao().getByComponentId(1);
        assertThat(eventKey).isNotNull();
        assertThat(eventKey.getComponentId()).isEqualTo(1);
        assertThat(eventKey.getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(eventKey.getKey()).isEqualTo("outboxInsertKey");
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Outbox Component")
    void testUpdate() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var outbox = TestHelpers.createOutboxComponent();
        save(outbox);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);
        assertThat(eventKeyDao().size()).isEqualTo(1);

        assertThat(pinsDao.getLastValueById(1)).isNull(); // not set yet!

        // ---------------------------------
        // Update Statement #1
        //   * Submit a value to the outbox component
        //
        // Here we expect that the last value has been stored correctly
        // ---------------------------------
        outbox.getInputPin("data").setValue("updateValueChangeValue");
        save(outbox);

        // Verification after 1st update
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);
        assertThat(eventKeyDao.size()).isEqualTo(1);

        assertThat(pinsDao.getLastValueById(1)).isEqualTo("updateValueChangeValue");

        // ---------------------------------
        // Update Statement #2
        //   * Submit a value to the outbox component
        //
        // Here we expect that the last value has been stored correctly
        // ---------------------------------
        outbox.getInputPin("data").setValue("updateValueChangeValue_2");
        save(outbox);

        // Verification after 1st update
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);
        assertThat(eventKeyDao.size()).isEqualTo(1);

        assertThat(pinsDao.getLastValueById(1)).isEqualTo("updateValueChangeValue_2");
    }

}

