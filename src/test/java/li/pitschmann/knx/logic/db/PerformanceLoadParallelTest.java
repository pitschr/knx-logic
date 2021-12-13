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

import experimental.api.ComponentFactory;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.db.loader.LogicComponentLoader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;

/**
 * Performance test how fast the components are loaded from database in parallel
 * <p><p><p>
 * <strong>History:</strong><br>
 * [Result | 2021-03-03] Total(10000)=12.1250s / Average=1.21ms
 */
@Disabled
class PerformanceLoadParallelTest extends BaseDatabaseSuite {
    private final static LogicComponentLoader LOADER =
            new LogicComponentLoader(databaseManager, mock(ComponentFactory.class));
    private final static int TIMES = 1000;
    private final static int ITERATIONS = 10;

    @Test
    @DisplayName("Performance Test: Load components in parallel")
    void loadParallel() {
        executeSqlFile(new File(Sql.Logic.H));

        // warm-up
        for (int i = 0; i < 1000; i++) {
            LOADER.loadById(1);
        }

        // test
        final var start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            loadParallelInternal();
        }
        final var duration = System.currentTimeMillis() - start;
        System.out.printf("[Result | %s] Total(%s)=%.4fs / Average=%.2fms%n",
                LocalDate.now(),
                TIMES * ITERATIONS,
                (duration / 1000d),
                (duration / ((double) ITERATIONS * TIMES))
        );
    }

    private void loadParallelInternal() {
        final var futures = new ArrayList<CompletableFuture<LogicComponent>>(TIMES);
        for (int i = 0; i < TIMES; i++) {
            futures.add(CompletableFuture.supplyAsync(() -> LOADER.loadById(1)));
        }

        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[TIMES])).get(); // blocking!
        } catch (Throwable t) {
            throw new AssertionError("Execution failed in loadParallelInternal()", t);
        }
    }
}
