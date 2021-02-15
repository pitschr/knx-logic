package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT18;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT18}
 *
 * @author PITSCHR
 */
public final class DPT18Outbox implements Outbox {
    @Input
    private boolean controlled;

    @Input
    private int sceneNumber;

    @Override
    public DataPointValue getData() {
        return DPT18.SCENE_CONTROL.of(controlled, sceneNumber);
    }
}
