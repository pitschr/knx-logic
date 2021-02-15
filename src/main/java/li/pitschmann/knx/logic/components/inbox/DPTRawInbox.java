package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for KNX raw data / byte arrays.
 * <p>
 * This component can be used for unsupported
 * KNX Data Point Types
 *
 * @author PITSCHR
 */
public class DPTRawInbox implements Inbox {
    @Output
    private byte[] bytes;

    @Override
    public void accept(final Object data) {
        bytes = data instanceof byte[] ? ((byte[]) data).clone() : new byte[0];
    }

}
