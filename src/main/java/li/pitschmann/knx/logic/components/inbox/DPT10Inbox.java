package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT10;
import li.pitschmann.knx.logic.annotations.Output;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Inbox for KNX {@link DPT10}
 *
 * @author PITSCHR
 */
public class DPT10Inbox implements Inbox {
    @Output
    private DayOfWeek dayOfWeek;

    @Output
    private LocalTime time;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT10.TIME_OF_DAY.of((byte[]) data);
        dayOfWeek = dptValue.getDayOfWeek();
        time = dptValue.getTime();
    }

}
