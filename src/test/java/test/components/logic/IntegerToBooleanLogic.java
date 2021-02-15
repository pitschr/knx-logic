package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Converts a type of {@code Integer} to {@code Boolean} (digit).
 * <p>
 * <u>Matrix:</u><br>
 * <ul>
 * <li>If <i>input</i> is {@code 0} (digit: zero), then <i>output</i> is {@code false} (boolean)</li>
 * <li>If <i>input</i> is {@code 1} (digit: one), then <i>output</i> is {@code true} (boolean)</li>
 * <li>If <i>input</i> is an another digit (e.g. 2, 13, -5, ...), then <i>output</i> is {@code false} (boolean)</li>
 * </ul>
 *
 * @author PITSCHR
 */
public class IntegerToBooleanLogic implements Logic {
    @Input
    private Integer input;

    @Output
    private Boolean output;

    @Override
    public void logic() {
        this.output = this.input != null && this.input.intValue() == 1;
    }
}
