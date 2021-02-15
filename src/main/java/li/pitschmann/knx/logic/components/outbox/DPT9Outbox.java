package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT9;
import li.pitschmann.knx.core.datapoint.value.DPT9Value;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT9Value}
 *
 * @author PITSCHR
 */
public final class DPT9Outbox implements Outbox {
    @Input
    private double floatValue;

    @Override
    public DataPointValue getData() {
        return DPT9.AIR_FLOW.of(floatValue);
    }
}

