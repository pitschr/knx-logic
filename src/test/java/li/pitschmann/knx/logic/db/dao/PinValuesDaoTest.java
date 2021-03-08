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

package li.pitschmann.knx.logic.db.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static test.assertions.model.DatabaseAssertions.assertThat;

/**
 * Test case for writing logic components to database
 * {@link li.pitschmann.knx.logic.db.dao.PinValuesDao}
 *
 * @author PITSCHR
 */
class PinValuesDaoTest extends BaseDatabaseSuite {

    @Test
    @DisplayName("No last value found")
    void testNoLastValue() {
        final var pinValue = pinValuesDao().lastValueByPinId(999);
        assertThat(pinValue).isNull();
    }

    @Test
    @DisplayName("Last value for single pin value entry")
    void testLastValueSingle() {
        executeSqlFile(new File(Sql.PinValues.LAST_VALUE_SINGLE));

        final var inputValue = pinValuesDao().lastValueByPinId(1);
        assertThat(inputValue).isInstanceOf(Integer.class);
        assertThat((Integer) inputValue).isEqualTo(4711);

        final var outputValue = pinValuesDao().lastValueByPinId(2);
        assertThat(outputValue).isInstanceOf(Integer.class);
        assertThat((Integer) outputValue).isEqualTo(1477);
    }

    @Test
    @DisplayName("Last value for multiple pin value entries - just return the last one for each pin id")
    void testLastValueMultiple() {
        executeSqlFile(new File(Sql.PinValues.LAST_VALUE_MULTIPLE));

        final var inputValue = pinValuesDao().lastValueByPinId(1);
        assertThat(inputValue).isInstanceOf(Integer.class);
        assertThat((Integer) inputValue).isEqualTo(4921);

        final var outputValue = pinValuesDao().lastValueByPinId(2);
        assertThat(outputValue).isInstanceOf(Integer.class);
        assertThat((Integer) outputValue).isEqualTo(7717);
    }

    @Test
    @DisplayName("Last value with several value types")
    void testLastValueTypeObject() {
        executeSqlFile(new File(Sql.PinValues.LAST_VALUE_TYPES));

        final var inputValue = pinValuesDao().lastValueByPinId(1);
        assertThat(inputValue).isInstanceOf(BigDecimal.class);
        assertThat((BigDecimal) inputValue).isEqualTo(
                new BigDecimal("123456789012345678901234567890123456789012345678901234567890.0987654321")
        );

        final var outputValue = pinValuesDao().lastValueByPinId(2);
        assertThat(outputValue).isInstanceOf(String.class);
        assertThat((String) outputValue).isEqualTo("I am a string!");
    }

    @Test
    @DisplayName("Insert value by value and value type")
    void testInsertValueAndValueClass() {
        executeSqlFile(new File(Sql.PinValues.NO_PIN_VALUES));
        final var dao = pinValuesDao();

        dao.insert(1, "Hello", Object.class);
        dao.insert(1, "1234", Long.class);

        final var maps = jdbi().withHandle(handle ->
                handle.createQuery("SELECT value, valueType FROM pin_values").mapToMap().list()
        );

        assertThat(maps.get(0)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "Hello", "valuetype", "java.lang.Object")
        );

        assertThat(maps.get(1)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "1234", "valuetype", "java.lang.Long")
        );
    }

    @Test
    @DisplayName("Insert value by value only")
    void testInsertValue() {
        executeSqlFile(new File(Sql.PinValues.NO_PIN_VALUES));
        final var dao = pinValuesDao();

        dao.insert(1, "World");
        dao.insert(1, (byte) 0b0001_0001);
        dao.insert(1, true);
        dao.insert(1, 123);
        dao.insert(1, 321L);
        dao.insert(1, null);

        final var maps = jdbi().withHandle(handle ->
                handle.createQuery("SELECT value, valueType FROM pin_values").mapToMap().list()
        );

        assertThat(maps.get(0)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "World", "valuetype", "java.lang.String")
        );

        assertThat(maps.get(1)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "17", "valuetype", "java.lang.Byte")
        );

        assertThat(maps.get(2)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "true", "valuetype", "java.lang.Boolean")
        );

        assertThat(maps.get(3)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "123", "valuetype", "java.lang.Integer")
        );

        assertThat(maps.get(4)).containsExactlyInAnyOrderEntriesOf(
                Map.of("value", "321", "valuetype", "java.lang.Long")
        );

        assertThat(maps.get(5)).containsExactlyInAnyOrderEntriesOf(
                new HashMap<>() {{
                    put("value", null);
                    put("valuetype", null);
                }}
        );
    }
}
