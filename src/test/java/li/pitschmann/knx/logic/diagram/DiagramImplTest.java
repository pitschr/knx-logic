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

package li.pitschmann.knx.logic.diagram;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link DiagramImpl}
 */
class DiagramImplTest {

    @Test
    @DisplayName("Test Diagram implementations")
    void testGeneral() {
        final var diagram = new DiagramImpl();
        diagram.setName("DIAGRAM NAME");
        diagram.setDescription("DIAGRAM DESCRIPTION");

        assertThat(diagram.getUid()).isNotNull();
        assertThat(diagram.getName()).isEqualTo("DIAGRAM NAME");
        assertThat(diagram.getDescription()).isEqualTo("DIAGRAM DESCRIPTION");

        assertThat(diagram).hasToString(
                String.format(
                        "DiagramImpl{" + //
                                "uid=%s, " + //
                                "name=DIAGRAM NAME, " + //
                                "description=DIAGRAM DESCRIPTION" + //
                                "}",
                        diagram.getUid()
                )
        );
    }
}
