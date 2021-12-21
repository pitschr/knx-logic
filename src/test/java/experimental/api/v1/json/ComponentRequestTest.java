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

package experimental.api.v1.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link ComponentRequest}
 */
class ComponentRequestTest {
    @Test
    @DisplayName("Test getter/setters and toString()")
    void testBasic() {
        final var request = new ComponentRequest();
        request.setType("TYPE");
        request.setEvent("EVENT");
        request.setData(Map.of("KEY", "VALUE"));

        assertThat(request.getType()).isEqualTo("TYPE");
        assertThat(request.getEvent()).isEqualTo("EVENT");
        assertThat(request.getData()).hasSize(1).containsEntry("KEY", "VALUE");

        assertThat(request).hasToString(
                "ComponentRequest{type=TYPE, event=EVENT, data={KEY=VALUE}}"
        );
    }
}
