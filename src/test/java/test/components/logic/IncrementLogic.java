package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Increments the {@code input} by {@code 1}
 */
public final class IncrementLogic implements Logic {
    @Input
    private int input;

    @Output
    private int output;

    @Override
    public void logic() {
        this.output = this.input + 1;
    }
}
