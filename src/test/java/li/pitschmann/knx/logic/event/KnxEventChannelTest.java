package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.core.communication.KnxClient;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test case for {@link KnxEventChannel}
 *
 * @author PITSCHR
 */
public final class KnxEventChannelTest {

    @Test
    @DisplayName("Test KNX Event ChannelId")
    public void testChannelId() {
        final var mockKnxClient = mock(KnxClient.class);
        final var eventChannel = new KnxEventChannel(mockKnxClient);

        assertThat(eventChannel.getChannel()).isSameAs(KnxEventChannel.CHANNEL_ID);
    }

    @Test
    @DisplayName("Test KNX Event Channel Outbound")
    public void testOutbound() {
        final var mockKnxClient = mock(KnxClient.class);
        final var eventChannel = new KnxEventChannel(mockKnxClient);
        final var eventKeyMock = mock(EventKey.class);
        when(eventKeyMock.getIdentifier()).thenReturn("6/7/8");
//
//        // ---------------------------------------
//        // Test + Verification (no APCI Data)
//        // ---------------------------------------
//        final var eventNoApciData = new Event(eventKeyMock, new byte[0]);
//        assertThat(eventNoApciData.getKey()).isSameAs(eventKeyMock);
//        assertThat((byte[]) eventNoApciData.getData()).isEmpty();
//
//        eventChannel.outbound(eventNoApciData);
//        verify(mockKnxClient).writeRequest(any(GroupAddress.class), eq(DataPointValue.c));
//
//        // ---------------------------------------
//        // Test + Verification (with APCI Data)
//        // ---------------------------------------
//        final var apciData = new byte[]{0x22, 0x23, 0x24};
//        final var eventWithApciData = new Event(eventKeyMock, apciData);
//
//        eventChannel.outbound(eventWithApciData);
//        verify(mockKnxClient).writeRequest(any(GroupAddress.class), eq(apciData));
    }

}
