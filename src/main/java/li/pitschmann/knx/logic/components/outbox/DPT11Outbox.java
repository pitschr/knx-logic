package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT11;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

import java.time.LocalDate;

/**
 * Outbox for KNX {@link DPT11}
 *
 * @author PITSCHR
 */
public final class DPT11Outbox implements Outbox {
    @Input
    private LocalDate date;

    @Override
    public DataPointValue getData() {
        return DPT11.DATE.of(date);
    }
}

