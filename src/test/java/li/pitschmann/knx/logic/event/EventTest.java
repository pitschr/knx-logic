package li.pitschmann.knx.logic.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link Event}
 */
public class EventTest {

    @Test
    @DisplayName("Test Event instance")
    public void testEvent() {
        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.getChannel()).thenReturn("CHANNEL");
        when(eventKeyMock.getIdentifier()).thenReturn("IDENTIFIER");
        final var value = List.of(1, 2, 3);

        final var event = new Event(eventKeyMock, value);
        assertThat(event.getKey()).isSameAs(eventKeyMock);
        assertThat(event.getData()).isSameAs(value);

        // string representation
        assertThat(event).hasToString("" + //
                "Event{" + //
                "channel=CHANNEL, " + //
                "identifier=IDENTIFIER, " + //
                "data=[1, 2, 3]" + //
                "}" //
        );
    }

    @Test
    @DisplayName("Test Event with byte-array as value")
    public void testEventWithByteArray() {
        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.getChannel()).thenReturn("CHANNEL2");
        when(eventKeyMock.getIdentifier()).thenReturn("IDENTIFIER2");
        final var value = new byte[]{4, 5, 6};

        // string representation
        assertThat(new Event(eventKeyMock, value)).hasToString("" + //
                "Event{" + //
                "channel=CHANNEL2, " + //
                "identifier=IDENTIFIER2, " + //
                "data=0x04 05 06" + //
                "}" //
        );
    }

}
