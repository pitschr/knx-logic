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

package li.pitschmann.knx.logic.db.jdbi.mappers.row;

import li.pitschmann.knx.logic.exceptions.LoaderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link PinValueMapper}
 */
class PinValueMapperTest {

    @Test
    @DisplayName("Convert value to String")
    void testConversion_String() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString("value")).thenReturn("Hello World!");
        when(resultSetMock.getString("valueType")).thenReturn("java.lang.String");

        final var mapper = new PinValueMapper();
        final var value = mapper.map(resultSetMock, null);

        assertThat(value).isEqualTo("Hello World!");
    }

    @Test
    @DisplayName("Convert value to Integer")
    void testConversion_Integer() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString("value")).thenReturn("123456");
        when(resultSetMock.getString("valueType")).thenReturn("java.lang.Integer");

        final var mapper = new PinValueMapper();
        final var value = mapper.map(resultSetMock, null);

        assertThat(value).isInstanceOf(Integer.class).isEqualTo(123456);
    }

    @Test
    @DisplayName("Convert value to BigDecimal")
    void testConversion_BigDecimal() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString("value")).thenReturn("123456.7890123");
        when(resultSetMock.getString("valueType")).thenReturn("java.math.BigDecimal");

        final var mapper = new PinValueMapper();
        final var value = mapper.map(resultSetMock, null);

        assertThat(value).isEqualTo(new BigDecimal("123456.7890123"));
    }

    @Test
    @DisplayName("Conversion failure due unknown class")
    void testConversion_UnknownClass() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString("value")).thenReturn("123");
        when(resultSetMock.getString("valueType")).thenReturn("java.lang.UNKNOWN");

        final var mapper = new PinValueMapper();
        assertThatThrownBy(() -> mapper.map(resultSetMock, null))
                .isInstanceOf(LoaderException.class)
                .hasMessage("Could not find class 'java.lang.UNKNOWN' to be converted: 123");
    }

    @Test
    @DisplayName("Conversion failure due non-supported class by transformer")
    void testConversion_Unsupported() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString("value")).thenReturn("123");
        when(resultSetMock.getString("valueType")).thenReturn("java.lang.Void");

        final var mapper = new PinValueMapper();
        assertThatThrownBy(() -> mapper.map(resultSetMock, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find a matching transformer for: java.lang.Void");
    }
}
