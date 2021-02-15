package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Logic Component with 1 static input, 1 dynamic input,
 * 1 static output and 1 dynamic output
 *
 * @author PITSCHR
 */
public final class LogicG implements Logic {
    @Input
    private Boolean input;

    @Input
    private List<Boolean> inputs;

    @Output
    private boolean output;

    @Output
    private List<Boolean> outputs;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
