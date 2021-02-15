package li.pitschmann.knx.logic.db.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link EventKeyModel}
 */
public class EventKeyModelTest {

    @Test
    @DisplayName("Basic Test for EventKeyModel")
    public void test() {
        final var model = new EventKeyModel();
        model.setComponentId(6458);
        model.setChannel("CHANNEL");
        model.setKey("KEY");

        assertThat(model.getChannel()).isEqualTo("CHANNEL");
        assertThat(model.getKey()).isEqualTo("KEY");
        assertThat(model).hasToString("" + //
                "EventKeyModel{" + //
                "componentId=6458, " + //
                "channel=CHANNEL, " + //
                "key=KEY" + //
                "}"
        );
    }
}
