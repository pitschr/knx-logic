package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT5;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT5}
 *
 * @author PITSCHR
 */
public final class DPT5Outbox implements Outbox {
    @Input
    private int unsignedValue;

    @Override
    public DataPointValue getData() {
        return DPT5.VALUE_1_OCTET_UNSIGNED_COUNT.of(unsignedValue);
    }
}
