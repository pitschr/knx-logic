/*
 * Copyright (C) 2021 Pitschmann Christoph
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

package li.pitschmann.knx.logic.db;

import li.pitschmann.knx.logic.components.LogicComponentImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.components.LogicH;

import java.io.File;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Performance test how fast the components are loaded from database
 * <p><p><p>
 * <strong>History:</strong><br>
 * [Result | 2021-12-18] Total(10000)=17.7330s / Average=1.77ms / Min=1.00ms / Max=34.00ms
 * [Result | 2021-03-02] Total(10000)=17.3950s / Average=1.74ms / Min=1.00ms / Max=14.00ms
 */
@Disabled
class PerformanceLoadSequentialTest extends BaseDatabaseSuite {
    private final static int TIMES = 1000;
    private final static int ITERATIONS = 10;
    private static double min = Double.MAX_VALUE;
    private static double max = Double.MIN_VALUE;

    @Test
    @DisplayName("Performance Test: Load components")
    void loadSequentially() {
        executeSqlFile(new File(Sql.Logic.H));

        // warm-up
        for (int i = 0; i < 1000; i++) {
            loadComponentById(1);
        }

        // test
        final var start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            loadSequentiallyInternal();
        }
        final var duration = System.currentTimeMillis() - start;
        final var output = String.format("[Result | %s] Total(%s)=%.4fs / Average=%.2fms / Min=%.2fms / Max=%.2fms",
                LocalDate.now(),
                TIMES * ITERATIONS,
                (duration / 1000d),
                (duration / ((double) ITERATIONS * TIMES)),
                min,
                max
        );
        System.out.println(output);
    }

    private void loadSequentiallyInternal() {
        long start;
        long duration;
        Object component;
        for (int i = 0; i < TIMES; i++) {
            start = System.currentTimeMillis();
            component = loadComponentById(1);
            duration = System.currentTimeMillis() - start;
            assertThat(component).isInstanceOf(LogicComponentImpl.class);
            assertThat((LogicComponentImpl.class.cast(component)).getWrappedObject()).isInstanceOf(LogicH.class);
            min = Math.min(min, duration);
            max = Math.max(max, duration);
        }
    }
}
