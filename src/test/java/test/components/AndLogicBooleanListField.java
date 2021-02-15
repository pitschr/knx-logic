package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.Arrays;
import java.util.List;

/**
 * Test component with dynamic input that will be dynamically initialized directly
 */
public final class AndLogicBooleanListField implements Logic {
    @Input
    private List<Boolean> i = Arrays.asList(false, true); // initialized with 2 default values already!

    @Output
    private boolean o;

    @Override
    public void logic() {
        this.o = this.i.stream().allMatch(Boolean::booleanValue);
    }
}
