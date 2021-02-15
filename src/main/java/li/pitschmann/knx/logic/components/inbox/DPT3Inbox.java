package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT3;
import li.pitschmann.knx.core.datapoint.value.StepInterval;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT3}
 *
 * @author PITSCHR
 */
public class DPT3Inbox implements Inbox {
    @Output
    private boolean controlled;

    @Output
    private StepInterval stepInterval;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT3.DIMMING_CONTROL.of((byte[]) data);
        controlled = dptValue.isControlled();
        stepInterval = dptValue.getStepInterval();
    }

}
