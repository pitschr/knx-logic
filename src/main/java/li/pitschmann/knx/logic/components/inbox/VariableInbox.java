package li.pitschmann.knx.logic.components.inbox;

import li.pitschmann.knx.logic.annotations.Output;

/**
 * Inbox for Variable
 *
 * @author PITSCHR
 */
public class VariableInbox implements Inbox {

    @Output
    private Object data;

    @Override
    public void accept(final Object data) {
        this.data = data;
    }

}
