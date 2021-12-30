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

package li.pitschmann.knx.api.v1.services;

import li.pitschmann.knx.logic.descriptor.FieldDescriptor;
import li.pitschmann.knx.logic.pin.Pin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for {@link PinService}
 */
class PinServiceTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("Test #setValue(Pin, String) with String as value")
    void test_setValue_String() {
        final var service = new PinService();

        final var fieldDescriptor = mock(FieldDescriptor.class);
        doReturn(String.class).when(fieldDescriptor).getFieldType();

        final var pinMock = mock(Pin.class);
        when(pinMock.getDescriptor()).thenReturn(fieldDescriptor);
        service.setValue(pinMock, "foobar");

        // verify
        verify(pinMock).setValue(eq("foobar"));
    }

    @Test
    @DisplayName("Test #setValue(Pin, String) with Int as value")
    void test_setValue_Int() {
        final var service = new PinService();

        final var fieldDescriptor = mock(FieldDescriptor.class);
        doReturn(Integer.class).when(fieldDescriptor).getFieldType();

        final var pinMock = mock(Pin.class);
        when(pinMock.getDescriptor()).thenReturn(fieldDescriptor);
        service.setValue(pinMock, "123");

        // verify
        verify(pinMock).setValue(eq(123));
    }
}
