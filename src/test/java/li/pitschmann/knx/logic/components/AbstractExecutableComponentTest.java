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

package li.pitschmann.knx.logic.components;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.components.LogicA;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link AbstractExecutableComponent}
 */
class AbstractExecutableComponentTest {

    @Test
    @DisplayName("OK: AbstractExecutableComponent")
    void testSuccessful() {
        final var logic = new LogicA();
        final var component = new AbstractExecutableComponent<>(logic) {
            @Override
            protected void executeSafe() {
                // NO-OP
            }
        };

        assertThat(component.getUid()).isNotNull();
        assertThat(component.getWrappedObject()).isSameAs(logic);

        // not executed yet
        assertThat(component.executedCount()).isZero();
        assertThat(component.executedTime()).isZero();

        // execute it once time and then verify
        component.execute();
        assertThat(component.executedCount()).isEqualTo(1);
        assertThat(component.executedTime()).isNotZero();
    }

    @Test
    @DisplayName("ERROR: AbstractExecutableComponent with issue in execution")
    void testFailure() {
        final var logic = new LogicA();
        final var component = new AbstractExecutableComponent<>(logic) {
            @Override
            protected void executeSafe() {
                throw new RuntimeException("Boo!");
            }
        };

        // not executed yet
        assertThat(component.executedCount()).isZero();
        assertThat(component.executedTime()).isZero();

        // execute it once time and then verify - this one is throwing an exception
        assertThatThrownBy(component::execute).isInstanceOf(RuntimeException.class).hasMessage("Boo!");
        assertThat(component.executedCount()).isEqualTo(1);
        assertThat(component.executedTime()).isNotZero();
    }

}
