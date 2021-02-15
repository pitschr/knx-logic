package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT3;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.core.datapoint.value.StepInterval;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT3}
 *
 * @author PITSCHR
 */
public final class DPT3Outbox implements Outbox {
    @Input
    private boolean controlled;

    @Input
    private StepInterval stepInterval;

    @Override
    public DataPointValue getData() {
        return DPT3.DIMMING_CONTROL.of(controlled, stepInterval);
    }
}
