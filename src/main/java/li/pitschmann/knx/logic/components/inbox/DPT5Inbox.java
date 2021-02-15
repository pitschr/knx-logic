package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT5;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT5}
 *
 * @author PITSCHR
 */
public class DPT5Inbox implements Inbox {
    @Output
    private int unsignedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT5.VALUE_1_OCTET_UNSIGNED_COUNT.of((byte[]) data);
        unsignedValue = dptValue.getValue();
    }

}
