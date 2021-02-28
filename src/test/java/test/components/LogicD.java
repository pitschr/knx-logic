package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;

import java.util.List;

/**
 * Logic Component with 1 dynamic input, no outputs
 *
 * @author PITSCHR
 */
public final class LogicD implements Logic {
    @Input
    private List<Boolean> i;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
