package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.function.Function;

/**
 * !!! LOGIC FOR TESTING PURPOSES ONLY !!!
 * <p>
 * Converts from {@code input} to {@code output} using {@link Function}
 */
public class FunctionLogic<T, R> implements Logic {
    private final Function<T, R> function;
    @Input
    private T input;
    @Output
    private R output;

    public FunctionLogic(final Function<T, R> function) {
        this.function = function;
    }

    @Override
    public void logic() {
        output = function.apply(input);
    }
}
