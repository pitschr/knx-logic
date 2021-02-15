package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT4;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT4}
 *
 * @author PITSCHR
 */
public final class DPT4Outbox implements Outbox {
    @Input
    private char charValue;

    @Override
    public DataPointValue getData() {
        return DPT4.ISO_8859_1.of(charValue);
    }
}
