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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link TwoTupleUIDMapper}
 */
class TwoTupleUIDMapperTest {

    @Test
    @DisplayName("Fetch TwoTupleUID")
    void testTuple_toString() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(eq(1))).thenReturn("UID-1");
        when(resultSetMock.getString(eq(2))).thenReturn("UID-2");

        final var mapper = new TwoTupleUIDMapper();
        final var tuple = mapper.map(resultSetMock, null);

        assertThat(tuple.getUid1()).hasToString("UID-1");
        assertThat(tuple.getUid2()).hasToString("UID-2");
    }

    @Test
    @DisplayName("Fetch TwoTupleUID with NULL for 1st column")
    void testTuple_Null() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(eq(1))).thenReturn(null);
        when(resultSetMock.getString(eq(2))).thenReturn("UID-2");

        final var mapper = new TwoTupleUIDMapper();
        assertThatThrownBy(() -> mapper.map(resultSetMock, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Fetch TwoTupleUID with NULL for 2nd column")
    void testTuple_Null2() throws SQLException {
        final var resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getString(eq(1))).thenReturn("UID-1");
        when(resultSetMock.getString(eq(2))).thenReturn(null);

        final var mapper = new TwoTupleUIDMapper();
        assertThatThrownBy(() -> mapper.map(resultSetMock, null))
                .isInstanceOf(NullPointerException.class);
    }
}
