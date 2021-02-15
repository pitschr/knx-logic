package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT17;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT17}
 *
 * @author PITSCHR
 */
public final class DPT17Outbox implements Outbox {
    @Input
    private int sceneNumber;

    @Override
    public DataPointValue getData() {
        return DPT17.SCENE_NUMBER.of(sceneNumber);
    }
}
