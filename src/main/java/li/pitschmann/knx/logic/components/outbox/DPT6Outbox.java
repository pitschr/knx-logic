package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT6;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT6}
 *
 * @author PITSCHR
 */
public final class DPT6Outbox implements Outbox {
    @Input
    private int signedValue;

    @Override
    public DataPointValue getData() {
        return DPT6.VALUE_1_OCTET_COUNT.of(signedValue);
    }
}
