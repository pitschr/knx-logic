package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Converts a type of {@code Boolean} to {@code Integer} (digit).
 * <p>
 * <u>Matrix:</u><br>
 * <ul>
 * <li>If <i>input</i> is {@code true} (boolean), then <i>output</i> is {@code 1} (digit: one)</li>
 * <li>If <i>input</i> is {@code false} (boolean), then <i>output</i> is {@code 0} (digit: zero)</li>
 * </ul>
 */
public class BooleanToIntegerLogic implements Logic {
    @Input
    private Boolean input;

    @Output
    private Integer output;

    @Override
    public void logic() {
        this.output = this.input ? 1 : 0;
    }
}
