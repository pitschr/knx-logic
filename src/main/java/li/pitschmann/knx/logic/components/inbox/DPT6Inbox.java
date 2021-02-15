package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT6;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT6}
 *
 * @author PITSCHR
 */
public class DPT6Inbox implements Inbox {
    @Output
    private int signedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT6.VALUE_1_OCTET_COUNT.of((byte[]) data);
        signedValue = dptValue.getValue();
    }

}
