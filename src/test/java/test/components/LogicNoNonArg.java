package test.components;

import li.pitschmann.knx.logic.Logic;

/**
 * Test Component for {@link Logic} that has no non-arg constructor
 */
public class LogicNoNonArg implements Logic {

    public LogicNoNonArg(boolean unused) {
        // NO-OP
    }

    @Override
    public void logic() {
        // NO-OP
    }
}
