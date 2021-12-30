package li.pitschmann.knx.api.v1.strategies;

import li.pitschmann.knx.core.address.GroupAddress;
import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.logic.components.OutboxComponent;
import li.pitschmann.knx.logic.components.OutboxComponentImpl;
import li.pitschmann.knx.logic.components.outbox.DPT10Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT11Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT12Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT13Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT14Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT16Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT17Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT18Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT1Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT2Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT3Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT4Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT5Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT6Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT7Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT8Outbox;
import li.pitschmann.knx.logic.components.outbox.DPT9Outbox;
import li.pitschmann.knx.logic.components.outbox.DPTRawOutbox;
import li.pitschmann.knx.logic.components.outbox.Outbox;
import li.pitschmann.knx.logic.event.KnxEventChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link CreateStrategy} implementation for KNX {@link OutboxComponent}
 *
 * @author PITSCHR
 */
public class KnxOutboxCreateStrategy implements CreateStrategy<OutboxComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(KnxOutboxCreateStrategy.class);
    private static final Map<String, Supplier<Outbox>> dptMapping;
    private static final Supplier<Outbox> DEFAULT_OUTBOX_SUPPLIER = DPTRawOutbox::new;

    /*
     * Hardcoded mapping for supported KNX DataPointTypes
     *
     * Defaults to {@link #DEFAULT_OUTBOX_SUPPLIER} if the key could not be found
     */
    static {
        dptMapping = Maps.newHashMap(20);
        dptMapping.put("dpt-1", DPT1Outbox::new);
        dptMapping.put("dpt-2", DPT2Outbox::new);
        dptMapping.put("dpt-3", DPT3Outbox::new);
        dptMapping.put("dpt-4", DPT4Outbox::new);
        dptMapping.put("dpt-5", DPT5Outbox::new);
        dptMapping.put("dpt-6", DPT6Outbox::new);
        dptMapping.put("dpt-7", DPT7Outbox::new);
        dptMapping.put("dpt-8", DPT8Outbox::new);
        dptMapping.put("dpt-9", DPT9Outbox::new);
        dptMapping.put("dpt-10", DPT10Outbox::new);
        dptMapping.put("dpt-11", DPT11Outbox::new);
        dptMapping.put("dpt-12", DPT12Outbox::new);
        dptMapping.put("dpt-13", DPT13Outbox::new);
        dptMapping.put("dpt-14", DPT14Outbox::new);
        dptMapping.put("dpt-16", DPT16Outbox::new);
        dptMapping.put("dpt-17", DPT17Outbox::new);
        dptMapping.put("dpt-18", DPT18Outbox::new);
    }

    @Override
    public OutboxComponent apply(final Map<String, String> data) {
        final var groupAddressStr = Objects.requireNonNull(data.get("groupAddress"));
        final var groupAddress = GroupAddress.of(groupAddressStr);
        LOG.debug("Outbox Group Address: {}", groupAddress);

        final var dptStr = data.get("dpt");
        LOG.debug("Outbox for Data Point Type: {}", dptStr);

        final var outbox = Optional.ofNullable(dptMapping.get(dptStr)).orElse(DEFAULT_OUTBOX_SUPPLIER).get();
        LOG.debug("Outbox Component for group address '{}' and data point type '{}': {}", groupAddressStr, dptStr, outbox);

        return new OutboxComponentImpl(
                KnxEventChannel.createKey(groupAddress), //
                outbox                            //
        );
    }
}
