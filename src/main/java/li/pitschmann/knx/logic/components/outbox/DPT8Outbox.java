package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT8;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT8}
 *
 * @author PITSCHR
 */
public final class DPT8Outbox implements Outbox {
    @Input
    private int signedValue;

    @Override
    public DataPointValue getData() {
        return DPT8.VALUE_2_OCTET_COUNT.of(signedValue);
    }
}

