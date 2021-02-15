package experimental.api.v1.strategies;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.logic.components.InboxComponent;
import li.pitschmann.knx.logic.components.InboxComponentImpl;
import li.pitschmann.knx.logic.components.inbox.DPT10Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT11Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT12Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT13Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT14Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT16Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT17Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT18Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT1Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT2Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT3Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT4Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT5Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT6Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT7Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT8Inbox;
import li.pitschmann.knx.logic.components.inbox.DPT9Inbox;
import li.pitschmann.knx.logic.components.inbox.DPTRawInbox;
import li.pitschmann.knx.logic.components.inbox.Inbox;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link CreateStrategy} implementation for KNX {@link InboxComponent}
 *
 * @author PITSCHR
 */
public class KnxInboxCreateStrategy implements CreateStrategy<InboxComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(KnxInboxCreateStrategy.class);
    private static final Map<String, Supplier<Inbox>> dptMapping;
    private static final Supplier<Inbox> DEFAULT_INBOX_SUPPLIER = DPTRawInbox::new;

    /*
     * Hardcoded mapping for supported KNX DataPointTypes
     *
     * Defaults to {@link #DEFAULT_INBOX_SUPPLIER} if the key could not be found
     */
    static {
        dptMapping = Maps.newHashMap(20);
        dptMapping.put("dpt-1", DPT1Inbox::new);
        dptMapping.put("dpt-2", DPT2Inbox::new);
        dptMapping.put("dpt-3", DPT3Inbox::new);
        dptMapping.put("dpt-4", DPT4Inbox::new);
        dptMapping.put("dpt-5", DPT5Inbox::new);
        dptMapping.put("dpt-6", DPT6Inbox::new);
        dptMapping.put("dpt-7", DPT7Inbox::new);
        dptMapping.put("dpt-8", DPT8Inbox::new);
        dptMapping.put("dpt-9", DPT9Inbox::new);
        dptMapping.put("dpt-10", DPT10Inbox::new);
        dptMapping.put("dpt-11", DPT11Inbox::new);
        dptMapping.put("dpt-12", DPT12Inbox::new);
        dptMapping.put("dpt-13", DPT13Inbox::new);
        dptMapping.put("dpt-14", DPT14Inbox::new);
        dptMapping.put("dpt-16", DPT16Inbox::new);
        dptMapping.put("dpt-17", DPT17Inbox::new);
        dptMapping.put("dpt-18", DPT18Inbox::new);
    }

    @Override
    public InboxComponent apply(final Map<String, String> data) {
        final var groupAddressStr = Objects.requireNonNull(data.get("groupAddress"));
        final var groupAddress = GroupAddress.of(groupAddressStr);
        LOG.debug("Inbox Group Address: {}", groupAddress);

        final var dptStr = data.get("dpt");
        LOG.debug("Inbox for Data Point Type: {}", dptStr);

        final var inbox = Optional.ofNullable(dptMapping.get(dptStr)).orElse(DEFAULT_INBOX_SUPPLIER).get();
        LOG.debug("Inbox Component for group address '{}' and data point type '{}': {}", groupAddressStr, dptStr, inbox);

        return new InboxComponentImpl(
                KnxEventChannel.createKey(groupAddress),  //
                inbox                              //
        );
    }
}
