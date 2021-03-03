package li.pitschmann.knx.logic.db.strategies;

import li.pitschmann.knx.logic.components.inbox.VariableInbox;
import li.pitschmann.knx.logic.db.jdbi.mappers.BindingType;
import li.pitschmann.knx.logic.db.jdbi.mappers.ComponentType;
import li.pitschmann.knx.logic.event.VariableEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.TestHelpers;

import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing inbox components to database
 * {@link InboxComponentPersistenceStrategy}
 *
 * @author PITSCHR
 */
class InboxComponentPersistenceStrategyTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test the INSERT persistence of Inbox Component")
    void testInsert() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var inbox = TestHelpers.createInboxComponent("inboxInsertKey");
        save(inbox);

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
                .componentType(ComponentType.INBOX)
                .className(VariableInbox.class.getName());
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

        // ---------------------------------
        // Verification: Pins
        // ---------------------------------
        final var pin = pinsDao.getById(1);
        assertThat(pin)
                .id(1)
                .connectorId(1)
                .index(0);
        assertThat(pin.getUid()).isNotNull();

        // ---------------------------------
        // Verification: EventKey
        // ---------------------------------
        final var eventKey = eventKeyDao.getByComponentId(1);
        assertThat(eventKey).isNotNull();
        assertThat(eventKey.getComponentId()).isEqualTo(1);
        assertThat(eventKey.getChannel()).isEqualTo(VariableEventChannel.CHANNEL_ID);
        assertThat(eventKey.getKey()).isEqualTo("inboxInsertKey");
    }

    @Test
    @DisplayName("Test the UPDATE persistence of Inbox Component")
    void testUpdate() {
        final var componentDao = componentsDao();
        final var connectorsDao = connectorsDao();
        final var pinsDao = pinsDao();
        final var eventKeyDao = eventKeyDao();

        final var inbox = TestHelpers.createInboxComponent();
        save(inbox);

        // ---------------------------------
        // Verification before update (initial values)
        // ---------------------------------
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);
        assertThat(eventKeyDao().size()).isEqualTo(1);

        // ---------------------------------
        // Update Statement #1
        // ---------------------------------
        inbox.onNext("updateValueChangeValue");
        save(inbox);

        // Verification after 1st update - should remain same
        assertThat(componentDao.size()).isEqualTo(1);
        assertThat(connectorsDao.size()).isEqualTo(1);
        assertThat(pinsDao.size()).isEqualTo(1);
        assertThat(eventKeyDao.size()).isEqualTo(1);
    }

}

