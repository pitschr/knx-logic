package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT9;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT9}
 *
 * @author PITSCHR
 */
public class DPT9Inbox implements Inbox {
    @Output
    private double floatValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT9.AIR_FLOW.of((byte[]) data);
        floatValue = dptValue.getValue();
    }

}
