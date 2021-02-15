package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT1;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT1}
 *
 * @author PITSCHR
 */
public final class DPT1Outbox implements Outbox {
    @Input
    private boolean boolValue;

    @Override
    public DataPointValue getData() {
        return DPT1.SWITCH.of(boolValue);
    }
}
