package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT8;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT8}
 *
 * @author PITSCHR
 */
public class DPT8Inbox implements Inbox {
    @Output
    private int signedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT8.VALUE_2_OCTET_COUNT.of((byte[]) data);
        signedValue = dptValue.getValue();
    }

}
