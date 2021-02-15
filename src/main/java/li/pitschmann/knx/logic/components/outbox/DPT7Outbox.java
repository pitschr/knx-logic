package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT7;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT7}
 *
 * @author PITSCHR
 */
public final class DPT7Outbox implements Outbox {
    @Input
    private int unsignedValue;

    @Override
    public DataPointValue getData() {
        return DPT7.VALUE_2_OCTET_UNSIGNED_COUNT.of(unsignedValue);
    }
}

