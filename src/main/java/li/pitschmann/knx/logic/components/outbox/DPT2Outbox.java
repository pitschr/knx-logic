package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT2;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT2}
 *
 * @author PITSCHR
 */
public final class DPT2Outbox implements Outbox {
    @Input
    private boolean controlled;

    @Input
    private boolean boolValue;

    @Override
    public DataPointValue getData() {
        return DPT2.SWITCH_CONTROL.of(controlled, boolValue);
    }
}
