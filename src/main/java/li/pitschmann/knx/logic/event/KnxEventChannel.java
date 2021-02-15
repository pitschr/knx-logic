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

    @Override
    public String getChannel() {
        return CHANNEL_ID;
    }

    public static EventKey createKey(final GroupAddress groupAddress) {
        return new EventKey(CHANNEL_ID, groupAddress.getAddress());
    }

    @Override
    public void outbound(final Event event) {
        knxClient.writeRequest(
                GroupAddress.of(event.getKey().getIdentifier()), //
                (DataPointValue)event.getData() //
        );
    }
}
