package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.Trigger;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Refresh Logic:
 * <p>
 * The 'logic()' method is always triggered regardless if the input value
 * has been changed. Output is triggered only when value has been changed.
 */
public class RefreshAlwaysTriggerInputOnlyLogic implements Logic {
    @Input(trigger = Trigger.ALWAYS)
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
