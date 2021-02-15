package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT1;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT1}
 *
 * @author PITSCHR
 */
public class DPT1Inbox implements Inbox {
    @Output
    private boolean boolValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT1.SWITCH.of((byte[]) data);
        boolValue = dptValue.getValue();
    }

}
