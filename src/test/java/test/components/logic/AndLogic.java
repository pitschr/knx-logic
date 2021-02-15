package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * A simple form of 2-AND Gate.
 * <p>
 * The field {@code output} is {@code true} only when both inputs are {@code true}.
 * The field {@code outputNegation} is the negation value of {@code output}.
 */
public class AndLogic implements Logic {
    @Input(min = 2, max = 2)
    private List<Boolean> inputs;

    @Output
    private boolean output;

    @Output
    private boolean outputNegation = true;

    @Override
    public void logic() {
        output = inputs.get(0) && inputs.get(1);
        outputNegation = !output;
    }
}
