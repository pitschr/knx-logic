package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Test component with static inputs only. All inputs and outputs are Objects.
 */
public final class AndLogicBooleanObject implements Logic {
    @Input
    private Boolean i1; // not initialized (null) ... will be automatically filled with 'Boolean.FALSE'

    @Input
    private Boolean i2 = Boolean.TRUE;

    @Output
    private Boolean o;

    @Override
    public void logic() {
        this.o = this.i1 && this.i2;
    }
}
