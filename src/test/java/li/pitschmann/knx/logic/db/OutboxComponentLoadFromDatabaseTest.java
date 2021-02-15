package li.pitschmann.knx.logic.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for loading {@link li.pitschmann.knx.logic.components.OutboxComponent} components from database
 *
 * @author PITSCHR
 */
class OutboxComponentLoadFromDatabaseTest extends BaseDatabaseSuite {

    @Override
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        databaseManager.executeSqlFile(new File(Sql.INSERT_OUTBOX_COMPONENT_SAMPLES));
        assertThat(componentsDao().size()).isEqualTo(3);
        assertThat(connectorsDao().size()).isEqualTo(4);
        assertThat(pinsDao().size()).isEqualTo(4);
    }


    @Test
    @DisplayName("Outbox: KNX DPT-1 (bool value)")
    void testOutboxWithKnxDpt1() {
        final var component = objectsDao().getOutboxComponentById(1);

        assertThat(component.getUid()).hasToString("uid-component-outbox-A");
        assertThat(component.getInputConnectors()).hasSize(1);
        assertThat(component.getInputPins()).hasSize(1);
        assertThat(component.getInputPin("boolValue").getValue()).isEqualTo(Boolean.FALSE);
        assertThat(component.getInputPin("boolValue").getUid()).hasToString("uid-pin-outbox-A#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("8711");
    }

    @Test
    @DisplayName("Outbox: KNX DPT-2 (controlled, bool value)")
    void testOutboxWithKnxDpt2() {
        final var component = objectsDao().getOutboxComponentById(2);

        assertThat(component.getUid()).hasToString("uid-component-outbox-B");
        assertThat(component.getInputConnectors()).hasSize(2);
        assertThat(component.getInputPins()).hasSize(2);
        assertThat(component.getInputPin("controlled").getValue()).isEqualTo(Boolean.FALSE);
        assertThat(component.getInputPin("controlled").getUid()).hasToString("uid-pin-outbox-B#controlled");
        assertThat(component.getInputPin("boolValue").getValue()).isEqualTo(Boolean.TRUE);
        assertThat(component.getInputPin("boolValue").getUid()).hasToString("uid-pin-outbox-B#boolValue");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("knx");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("6733");
    }

    @Test
    @DisplayName("Outbox: VARIABLE (string)")
    void testOutboxWithVariableChannel() {
        final var component = objectsDao().getOutboxComponentById(3);

        assertThat(component.getUid()).hasToString("uid-component-outbox-C");
        assertThat(component.getInputConnectors()).hasSize(1);
        assertThat(component.getInputPins()).hasSize(1);
        assertThat(component.getInputPin("data").getValue()).isEqualTo("Hello Earth");
        assertThat(component.getInputPin("data").getUid()).hasToString("uid-pin-outbox-C#data");

        // input specific
        assertThat(component.getEventKey().getChannel()).isEqualTo("var");
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("barfoo");
    }
}
