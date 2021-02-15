package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT11;
import li.pitschmann.knx.logic.annotations.Output;

import java.time.LocalDate;

/**
 * Inbox for KNX {@link DPT11}
 *
 * @author PITSCHR
 */
public class DPT11Inbox implements Inbox {
    @Output
    private LocalDate date;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT11.DATE.of((byte[]) data);
        date = dptValue.getDate();
    }

}
