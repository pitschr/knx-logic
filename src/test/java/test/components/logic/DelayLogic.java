package test.components.logic;

import li.pitschmann.knx.core.utils.Sleeper;
import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * A delay logic. The {@code output} will have same value like {@code input}
 * after execution which includes a 3-Second-Delay.
 */
public final class DelayLogic<T> implements Logic {
    @Input
    private T input;

    @Output
    private T output;

    @Override
    public void logic() {
        Sleeper.milliseconds(3000); // 3 seconds
        this.output = this.input;
    }
}
