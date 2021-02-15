package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT14;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT14}
 *
 * @author PITSCHR
 */
public class DPT14Inbox implements Inbox {
    @Output
    private double floatValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT14.ACCELERATION.of((byte[]) data);
        floatValue = dptValue.getValue();
    }

}
