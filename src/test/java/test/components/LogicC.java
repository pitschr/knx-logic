package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Logic Component with 1 static input, 1 static output
 *
 * @author PITSCHR
 */
public final class LogicC implements Logic {
    @Input
    private boolean i;

    @Output
    private boolean o;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
