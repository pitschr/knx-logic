package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT2;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT2}
 *
 * @author PITSCHR
 */
public class DPT2Inbox implements Inbox {
    @Output
    private boolean controlled;

    @Output
    private boolean boolValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT2.SWITCH_CONTROL.of((byte[]) data);
        controlled = dptValue.isControlled();
        boolValue = dptValue.getValue();
    }

}
