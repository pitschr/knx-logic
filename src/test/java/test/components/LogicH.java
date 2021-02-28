package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.util.List;

/**
 * Logic Component with 8 dynamic inputs, 1 dynamic output
 *
 * @author PITSCHR
 */
public class LogicH implements Logic {
    @Input
    private List<Boolean> booleans; // Input #1, Total #1

    @Input
    private List<Byte> bytes; // Input #2, Total #2

    @Input
    private List<Character> chars; // Input #3, Total #3

    @Input
    private List<Double> doubles; // Input #4, Total #4

    @Input
    private List<Float> floats; // Input #5, Total #5

    @Input
    private List<Integer> integers; // Input #6, Total #6

    @Input
    private List<Long> longs; // Input #7, Total #7

    @Input
    private List<Short> shorts; // Input #8, Total #8

    @Output
    private List<String> strings; // Output #1, Total #9

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
