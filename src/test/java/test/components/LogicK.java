package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Logic Component with 2 dynamic inputs, 2 dynamic output
 *
 * @author PITSCHR
 */
public class LogicK implements Logic {
    @Input
    private List<Integer> inputFirst, inputSecond;

    @Output
    private List<String> outputFirst, outputSecond;

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
