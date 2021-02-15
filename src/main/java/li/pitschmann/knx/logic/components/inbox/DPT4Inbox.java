package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT4;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT4}
 *
 * @author PITSCHR
 */
public class DPT4Inbox implements Inbox {
    @Output
    private char charValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT4.ISO_8859_1.of((byte[]) data);
        charValue = dptValue.getCharacter();
    }

}
