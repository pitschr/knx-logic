package test.components.logic;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Test Logic Component for LogicComponentPersistenceStrategyTest
 */
public final class TextSizeLogic implements Logic {
    @Input(min = 2)
    private List<String> texts;

    @Output
    private int totalTextLength,
                numberOfTexts;

    @Output
    private BigDecimal textAverageSize;

    @Override
    public void logic() {
        totalTextLength = texts.stream().mapToInt(String::length).sum();
        numberOfTexts = texts.size();

        // average size of all texts with a precision setting of 2 digits
        textAverageSize = new BigDecimal(totalTextLength)
                .divide(BigDecimal.valueOf(numberOfTexts), 2, RoundingMode.HALF_UP);
    }
}

