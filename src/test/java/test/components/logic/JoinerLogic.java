package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Joins all inputs to a single string representation (separated by single white space character)
 */
public class JoinerLogic implements Logic {
    @Input(min = 2, max = 4)
    private List<String> inputs;

    @Output
    private String output;

    @Override
    public void logic() {
        output = String.join(" ", inputs).trim();
    }
}
