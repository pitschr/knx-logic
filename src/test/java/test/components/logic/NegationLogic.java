package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Negates the bool value from {@code input} (false -> true; true -> false)
 */
public class NegationLogic implements Logic {
    @Input
    private boolean input;

    @Output
    private boolean output;

    @Override
    public void logic() {
        output = !input;
    }
}
