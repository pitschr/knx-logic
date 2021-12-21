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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test for {@link PinSetValueRequest}
 */
class PinSetValueRequestTest {
    @Test
    @DisplayName("Test getter/setters and toString()")
    void testBasic() {
        final var request = new PinSetValueRequest();
        request.setValue("VALUE");

        assertThat(request.getValue()).isEqualTo("VALUE");
        assertThat(request).hasToString("PinSetValueRequest{value=VALUE}");
    }
}
