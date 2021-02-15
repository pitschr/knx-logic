package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.DPT16;
import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX {@link DPT16}
 *
 * @author PITSCHR
 */
public final class DPT16Outbox implements Outbox {
    @Input
    private String stringValue;

    @Override
    public DataPointValue getData() {
        return DPT16.ISO_8859_1.of(stringValue);
    }
}

