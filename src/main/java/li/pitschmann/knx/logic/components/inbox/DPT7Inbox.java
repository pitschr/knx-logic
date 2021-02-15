package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT7;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT7}
 *
 * @author PITSCHR
 */
public class DPT7Inbox implements Inbox {
    @Output
    private int unsignedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT7.VALUE_2_OCTET_UNSIGNED_COUNT.of((byte[]) data);
        unsignedValue = dptValue.getValue();
    }

}
