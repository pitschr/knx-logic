package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;

/**
 * Logic Component with 1 static input (primitive), no outputs
 *
 * @author PITSCHR
 */
public final class LogicB implements Logic {
    @Input
    private boolean i;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
