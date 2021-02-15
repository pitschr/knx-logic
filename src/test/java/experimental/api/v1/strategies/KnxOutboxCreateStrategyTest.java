package experimental.api.v1.strategies;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.logic.components.outbox.DPT7Outbox;
import li.pitschmann.knx.logic.components.outbox.DPTRawOutbox;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link KnxOutboxCreateStrategy}
 */
public class KnxOutboxCreateStrategyTest {

    @Test
    @DisplayName("Test KNX Outbox Strategy with Group Address and Raw Data Point Type")
    public void createWithGroupAddress() {
        final var groupAddress = GroupAddress.of(5332).getAddress();
        final var data = Map.of("groupAddress", groupAddress);

        final var strategy = new KnxOutboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("5332");

        // the wrapped component is a KNX Raw Outbox which contains raw/byte-array output connector
        final var wrappedComponent = component.getInputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(DPTRawOutbox.class);
    }

    @Test
    @DisplayName("Test KNX Outbox Strategy with specified Data Point Type")
    public void createWithDPT() {
        final var groupAddress = GroupAddress.of(12232).getAddressLevel3();
        final var data = Map.of(
                "groupAddress", groupAddress, //
                "dpt", "dpt-7"
        );

        final var strategy = new KnxOutboxCreateStrategy();
        final var component = strategy.apply(data);

        assertThat(component).isNotNull();
        assertThat(component.getEventKey().getChannel()).isEqualTo(KnxEventChannel.CHANNEL_ID);
        assertThat(component.getEventKey().getIdentifier()).isEqualTo("12232");

        // the wrapped component is a DPT-7 Inbox which contains unsigned output connector
        final var wrappedComponent = component.getInputConnector(0).getDescriptor().getOwner();
        assertThat(wrappedComponent).isInstanceOf(DPT7Outbox.class);
    }

}
