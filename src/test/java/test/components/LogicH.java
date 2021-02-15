package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

/**
 * Logic Component with 8 static inputs, 9 static outputs
 *
 * @author PITSCHR
 */
public class LogicH implements Logic {
    @Input
    private boolean inputBooleanPrimitive; // Input #1, Total #1

    @Output
    private Boolean outputBooleanObject; // Output #1, Total #2

    @Input
    private byte inputBytePrimitive; // Input #2, Total #3

    @Output
    private Byte outputByteObject; // Output #2, Total #4

    @Input
    private char inputCharacterPrimitive; // Input #3, Total #5

    @Output
    private Character outputCharacterObject; // Output #3, Total #6

    @Input
    private double inputDoublePrimitive; // Input #4, Total #7

    @Output
    private Double outputDoubleObject; // Output #4, Total #8

    @Input
    private float inputFloatPrimitive; // Input #5, Total #9

    @Output
    private Float outputFloatObject; // Output #5, Total #10

    @Input
    private int inputIntegerPrimitive; // Input #6, Total #11

    @Output
    private Integer outputIntegerObject; // Output #6, Total #12

    @Input
    private long inputLongPrimitive; // Input #7, Total #13

    @Output
    private Long outputLongObject; // Output #7, Total #14

    @Input
    private short inputShortPrimitive; // Input #8, Total #15

    @Output
    private Short outputShortObject; // Output #8, Total #16

    @Output
    private String outputString; // Output #9, Total #17

    @Override
    public void logic() {
        // NO-OP (not a part of test)
    }
}
