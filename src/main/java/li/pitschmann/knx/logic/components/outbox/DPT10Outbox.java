package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT10;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Outbox for KNX {@link DPT10}
 *
 * @author PITSCHR
 */
public final class DPT10Outbox implements Outbox {
    @Input
    private DayOfWeek dayOfWeek;

    @Input
    private LocalTime time;

    @Override
    public DataPointValue getData() {
        return DPT10.TIME_OF_DAY.of(dayOfWeek, time);
    }
}

