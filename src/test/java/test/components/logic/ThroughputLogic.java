package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Just a throughput logic. The {@code output} has same value like {@code input} after execution
 * No effect.
 */
public final class ThroughputLogic<T> implements Logic {
    @Input
    private T input;

    @Output
    private T output;

    @Override
    public void logic() {
        this.output = this.input;
    }
}
