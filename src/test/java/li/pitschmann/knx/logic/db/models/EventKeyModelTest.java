package li.pitschmann.knx.logic.db.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link EventKeyModel}
 */
class EventKeyModelTest {

    @Test
    @DisplayName("Basic Test for EventKeyModel")
    void test() {
        final var model = EventKeyModel.builder()
                .componentId(6458)
                .channel("CHANNEL")
                .key("KEY")
                .build();

        assertThat(model.getChannel()).isEqualTo("CHANNEL");
        assertThat(model.getKey()).isEqualTo("KEY");
        assertThat(model).hasToString("" + //
                "EventKeyModel{" + //
                "id=-1, " + // -1 because of not persisted
                "componentId=6458, " + //
                "channel=CHANNEL, " + //
                "key=KEY" + //
                "}"
        );
    }
}
