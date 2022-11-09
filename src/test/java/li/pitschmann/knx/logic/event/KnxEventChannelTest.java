/*
 * Copyright (C) 2022 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package li.pitschmann.knx.logic.event;

import li.pitschmann.knx.core.communication.KnxClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
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
