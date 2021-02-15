package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.Arrays;
import java.util.List;

/**
 * Test component with dynamic input that will be dynamically initialized by constructor
 */
public final class AndLogicBooleanListConstructor implements Logic {
    @Input
    private final List<Boolean> i;

    @Output
    private boolean o;

    public AndLogicBooleanListConstructor() {
        this.i = Arrays.asList(false, true); // initialized with 2 default values already!
    }

    @Override
    public void logic() {
        this.o = this.i.stream().allMatch(Boolean::booleanValue);
    }
}
