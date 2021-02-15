package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT12;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT12}
 *
 * @author PITSCHR
 */
public final class DPT12Outbox implements Outbox {
    @Input
    private long unsignedValue;

    @Override
    public DataPointValue getData() {
        return DPT12.VALUE_4_OCTET_UNSIGNED_COUNT.of(unsignedValue);
    }
}

