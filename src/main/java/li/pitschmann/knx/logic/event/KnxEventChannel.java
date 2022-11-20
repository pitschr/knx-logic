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

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.core.communication.KnxClient;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;

import java.util.Objects;

/**
 * Channel for KNX events. This class is immutable.
 *
 * @author PITSCHR
 */
public final class KnxEventChannel implements EventChannel {
    public static final String CHANNEL_ID = "knx";
    private final KnxClient knxClient;

    public KnxEventChannel(final KnxClient knxClient) {
        this.knxClient = Objects.requireNonNull(knxClient);
    }

    public static EventKey createKey(final GroupAddress groupAddress) {
        return new EventKey(CHANNEL_ID, groupAddress.getAddress());
    }

    @Override
    public String getChannel() {
        return CHANNEL_ID;
    }

    @Override
    public void outbound(final Event event) {
        knxClient.writeRequest(
                GroupAddress.of(event.getKey().getIdentifier()), //
                (DataPointValue) event.getData() //
        );
    }
}
