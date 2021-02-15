package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Refresh Logic:
 * <p>
 * The 'logic()' method is only triggered if the input value has been changed.
 * No effect for output.
 */
public class RefreshDefaultTriggerLogic implements Logic {
    @Input
    private String input;

    @Output
    private String output;

    @Output
    private String output2;

    @Override
    public void logic() {
        // output #1: appending
        if (!output.isEmpty()) {
            output += ",";
        }
        output += input;

        // output #2: pass-through only
        output2 = input;
    }
}
