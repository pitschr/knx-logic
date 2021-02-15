package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Logic Component with 1 static input (object), no outputs
 *
 * @author PITSCHR
 */
public final class LogicD implements Logic {
    @Input
    private Boolean i;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
