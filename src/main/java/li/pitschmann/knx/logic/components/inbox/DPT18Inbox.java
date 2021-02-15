package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT18;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT18}
 *
 * @author PITSCHR
 */
public class DPT18Inbox implements Inbox {
    @Output
    private boolean controlled;

    @Output
    private int sceneNumber;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT18.SCENE_CONTROL.of((byte[]) data);
        controlled = dptValue.isControlled();
        sceneNumber = dptValue.getSceneNumber();
    }

}
