package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Logic Component with
 * - 1 static input
 * - 1 dynamic input,
 * - 1 static output and
 * - 1 dynamic output
 *
 * @author PITSCHR
 */
public final class LogicF implements Logic {
    @Input
    private String input;

    @Input
    private List<String> inputs;

    @Output
    private String output;

    @Output
    private List<String> outputs;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
