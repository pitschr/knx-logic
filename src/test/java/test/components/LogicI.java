package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Logic Component with 2 static inputs, 2 static output
 *
 * @author PITSCHR
 */
public class LogicI implements Logic {
    @Input
    private int inputFirst, inputSecond;

    @Output
    private String outputFirst, outputSecond;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
