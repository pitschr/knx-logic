package li.pitschmann.knx.logic.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for loading {@link li.pitschmann.knx.logic.components.InboxComponent} components from database
 *
 * @author PITSCHR
 */
class InboxComponentLoadFromDatabaseTest extends BaseDatabaseSuite {

    @Override
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        databaseManager.executeSqlFile(new File(Sql.INSERT_INBOX_COMPONENT_SAMPLES));
        assertThat(componentsDao().size()).isEqualTo(3);
        assertThat(connectorsDao().size()).isEqualTo(4);
        assertThat(pinsDao().size()).isEqualTo(4);
    }

    @Test
    @DisplayName("Inbox: KNX DPT-1 (bool value)")
    void testInboxWithKnxDpt1() {
        final var component = objectsDao().getInboxComponentById(1);

        assertThat(component.getUid()).hasToString("uid-component-inbox-A");
        assertThat(component.getOutputConnectors()).hasSize(1);
        assertThat(component.getOutputPins()).hasSize(1);
        assertThat(component.getOutputPin("boolValue").getValue()).isEqualTo(Boolean.TRUE);
        assertThat(component.getOutputPin("boolValue").getUid()).hasToString("uid-pin-inbox-A#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("4711");
    }

    @Test
    @DisplayName("Inbox: KNX DPT-2 (controlled, bool value)")
    void testInboxWithKnxDpt2() {
        final var component = objectsDao().getInboxComponentById(2);

        assertThat(component.getUid()).hasToString("uid-component-inbox-B");
        assertThat(component.getOutputConnectors()).hasSize(2);
        assertThat(component.getOutputPins()).hasSize(2);
        assertThat(component.getOutputPin("controlled").getValue()).isEqualTo(Boolean.TRUE);
        assertThat(component.getOutputPin("controlled").getUid()).hasToString("uid-pin-inbox-B#controlled");
        assertThat(component.getOutputPin("boolValue").getValue()).isEqualTo(Boolean.FALSE);
        assertThat(component.getOutputPin("boolValue").getUid()).hasToString("uid-pin-inbox-B#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("3171");
    }

    @Test
    @DisplayName("Inbox: VARIABLE (string)")
    void testInboxWithVariableChannel() {
        final var component = objectsDao().getInboxComponentById(3);

        assertThat(component.getUid()).hasToString("uid-component-inbox-C");
        assertThat(component.getOutputConnectors()).hasSize(1);
        assertThat(component.getOutputPins()).hasSize(1);
        assertThat(component.getOutputPin("data").getValue()).isEqualTo("Hello World");
        assertThat(component.getOutputPin("data").getUid()).hasToString("uid-pin-inbox-C#data");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("var");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("foobar");
    }
}
