package li.pitschmann.knx.logic.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link EventKey}
 */
public class EventKeyTest {

    @Test
    @DisplayName("Test the EventKey instance")
    public void testEventKey() {
        final var eventKey = new EventKey("Lorem", "Ipsum");

        assertThat(eventKey.getChannel()).isEqualTo("Lorem");
        assertThat(eventKey.getIdentifier()).isEqualTo("Ipsum");

        assertThat(eventKey).hasToString("EventKey{channel=Lorem, identifier=Ipsum}");
    }

    @Test
    @DisplayName("Test hashCode and equals of EventKey")
    public void testEventKeyEqualAndHashCode() {
        final var eventKeyA = new EventKey("Channel-A", "Hello");
        final var eventKeyB = new EventKey("Channel-A", "Hello"); // same like A
        final var eventKeyC = new EventKey("Channel-B", "Hello"); // different channel
        final var eventKeyD = new EventKey("Channel-A", "World"); // different value
        final var eventKeyE = new EventKey("Channel-B", "World"); // different channel and value
        final var otherObject = new Object();

        // equals
        assertThat(eventKeyA)
                .isEqualTo(eventKeyA)
                .isEqualTo(eventKeyB)
                .isNotEqualTo(eventKeyC)
                .isNotEqualTo(eventKeyD)
                .isNotEqualTo(eventKeyE)
                .isNotEqualTo(otherObject)
                .isNotEqualTo(null);

        // hashCode
        assertThat(eventKeyA)
                .hasSameHashCodeAs(eventKeyA)
                .hasSameHashCodeAs(eventKeyB);
        assertThat(eventKeyA.hashCode())
                .isNotEqualTo(eventKeyC)
                .isNotEqualTo(eventKeyD)
                .isNotEqualTo(eventKeyE)
                .isNotEqualTo(otherObject);
    }
}
