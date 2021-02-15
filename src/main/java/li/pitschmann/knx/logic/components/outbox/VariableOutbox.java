package li.pitschmann.knx.logic.components.outbox;

import li.pitschmann.knx.logic.annotations.Input;

/**
 * Outbox for Variable
 *
 * @author PITSCHR
 */
public final class VariableOutbox implements Outbox {
    @Input
    private Object data;

    @Override
    public Object getData() {
        return data;
    }
}
