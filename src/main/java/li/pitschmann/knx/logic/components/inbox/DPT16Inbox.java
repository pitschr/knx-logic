package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT16;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT16}
 *
 * @author PITSCHR
 */
public class DPT16Inbox implements Inbox {
    @Output
    private String stringValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT16.ISO_8859_1.of((byte[]) data);
        stringValue = dptValue.getCharacters();
    }

}
