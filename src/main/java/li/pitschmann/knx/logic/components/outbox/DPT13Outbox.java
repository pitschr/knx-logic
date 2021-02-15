package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT13;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT13}
 *
 * @author PITSCHR
 */
public final class DPT13Outbox implements Outbox {
    @Input
    private int signedValue;

    @Override
    public DataPointValue getData() {
        return DPT13.VALUE_4_OCTET_COUNT.of(signedValue);
    }
}

