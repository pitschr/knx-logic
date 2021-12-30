package li.pitschmann.knx.api.v1.strategies;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.logic.components.inbox.DPT5Inbox;
import li.pitschmann.knx.logic.components.inbox.DPTRawInbox;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link KnxInboxCreateStrategy}
 */
public class KnxInboxCreateStrategyTest {

    @Test
    @DisplayName("Test KNX Inbox Strategy with Group Address and Raw Data Point Type")
    public void createWithGroupAddress() {
        final var groupAddress = GroupAddress.of(9940).getAddress();
        final var data = Map.of("groupAddress", groupAddress);

        final var strategy = new KnxInboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("9940");

        // the wrapped component is a KNX Raw Inbox which contains raw/byte-array output connector
        final var wrappedComponent = component.getOutputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(DPTRawInbox.class);
    }

    @Test
    @DisplayName("Test KNX Inbox Strategy with specified Data Point Type")
    public void createWithDPT() {
        final var groupAddress = GroupAddress.of(12245).getAddressLevel3();
        final var data = Map.of(
                "groupAddress", groupAddress, //
                "dpt", "dpt-5"
        );

        final var strategy = new KnxInboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("12245");

        // the wrapped component is a DPT-5 Inbox which contains unsigned output connector
        final var wrappedComponent = component.getOutputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(DPT5Inbox.class);
    }

}
