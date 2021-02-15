package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT13;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT13}
 *
 * @author PITSCHR
 */
public class DPT13Inbox implements Inbox {
    @Output
    private int signedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT13.VALUE_4_OCTET_COUNT.of((byte[]) data);
        signedValue = dptValue.getValue();
    }

}
