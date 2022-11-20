/*
 * Copyright (C) 2022 Pitschmann Christoph
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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

