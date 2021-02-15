package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT17;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT17}
 *
 * @author PITSCHR
 */
public class DPT17Inbox implements Inbox {
    @Output
    private int sceneNumber;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT17.SCENE_NUMBER.of((byte[]) data);
        sceneNumber = dptValue.getSceneNumber();
    }

}
