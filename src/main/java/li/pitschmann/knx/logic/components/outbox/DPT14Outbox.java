package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT14;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT14}
 *
 * @author PITSCHR
 */
public final class DPT14Outbox implements Outbox {
    @Input
    private double floatValue;

    @Override
    public DataPointValue getData() {
        return DPT14.ACCELERATION.of(floatValue);
    }
}

