package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.core.datapoint.value.DataPointValue;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for KNX raw data / byte arrays.
 * <p>
 * This component can be used for unsupported
 * KNX Data Point Types
 *
 * @author PITSCHR
 */
public final class DPTRawOutbox implements Outbox {
    @Input
    private byte[] bytes;

    @Override
    public DataPointValue getData() {
        return () -> bytes == null ? new byte[0] : bytes.clone();
    }
}
