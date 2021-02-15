package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.core.datapoint.DPT12;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX {@link DPT12}
 *
 * @author PITSCHR
 */
public class DPT12Inbox implements Inbox {
    @Output
    private long unsignedValue;

    @Override
    public void accept(final Object data) {
        final var dptValue = DPT12.VALUE_4_OCTET_UNSIGNED_COUNT.of((byte[]) data);
        unsignedValue = dptValue.getValue();
    }

}
