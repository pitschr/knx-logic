package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Logic Component with 1 dynamic input, 1 dynamic output
 *
 * @author PITSCHR
 */
public final class LogicF implements Logic {
    @Input
    private List<Boolean> i;

    @Output
    private List<Boolean> o;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
