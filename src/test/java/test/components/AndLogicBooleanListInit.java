package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Test component with dynamic input that will be dynamically initialized with default values
 * Here it will be set 'true' during {@link #init()} method statement.
 */
public final class AndLogicBooleanListInit implements Logic {
    @Input(min = 2)
    private List<Boolean> i;

    @Output
    private boolean o;

    @Override
    public void init() {
        i.set(0, true);
    }

    @Override
    public void logic() {
        this.o = this.i.stream().allMatch(s -> s.booleanValue());
    }
}
