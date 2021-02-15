package li.pitschmann.knx.logic.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.TestHelpers;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test case for {@link KnxEventChannel}
 *
 * @author PITSCHR
 */
public final class VariableEventChannelTest {

    @Test
    @DisplayName("Test empty VARIABLE Event Channel")
    public void testChannelId() {
        final var eventChannel = new VariableEventChannel();

        assertThat(eventChannel.getChannel()).isSameAs(VariableEventChannel.CHANNEL_ID);
        assertThatThrownBy(() -> eventChannel.getCachedValueFor("foo")).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("Test VARIABLE Event Channel Outbound")
    public void testOutbound() {
        final var varName = "foobar";
        final var eventChannel = new VariableEventChannel();

        // ---------------------------------------
        // Test + Verification (with Value)
        // ---------------------------------------
        final var varValue = 7198;
        final var event = new Event(TestHelpers.createEventKey(varName), varValue);

        eventChannel.outbound(event);
        assertThat(eventChannel.getCachedValueFor(varName)).isEqualTo(varValue);

        // ---------------------------------------
        // Test + Verification (no Value)
        // ---------------------------------------
        final var event2 = new Event(TestHelpers.createEventKey(varName), "");

        eventChannel.outbound(event2);
        assertThat(eventChannel.getCachedValueFor(varName)).isEqualTo("");

        // ---------------------------------------
        // Test + Verification (with Value)
        // ---------------------------------------
        final var varValue2 = "foobar";
        final var event3 = new Event(TestHelpers.createEventKey(varName), varValue2);

        eventChannel.outbound(event3);
        assertThat(eventChannel.getCachedValueFor(varName)).isEqualTo(varValue2);
    }

}
