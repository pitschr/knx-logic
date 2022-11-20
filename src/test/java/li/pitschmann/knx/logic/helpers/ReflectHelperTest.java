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

package li.pitschmann.knx.logic.helpers;

import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import li.pitschmann.knx.logic.exceptions.ReflectException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link ReflectHelper}
 */
public class ReflectHelperTest {

    @Test
    @DisplayName("Filter declared fields by annotation")
    public void filterFieldsByAnnotations() {
        final var inputFields = ReflectHelper.filterFieldsByAnnotation(TestObject.class.getDeclaredFields(), Input.class);
        assertThat(inputFields).hasSize(2);
        assertThat(inputFields.get(0).getName()).isEqualTo("input1");
        assertThat(inputFields.get(1).getName()).isEqualTo("input2");

        final var outputFields = ReflectHelper.filterFieldsByAnnotation(TestObject.class.getDeclaredFields(), Output.class);
        assertThat(outputFields).hasSize(1);
        assertThat(outputFields.get(0).getName()).isEqualTo("output");
    }

    @Test
    @DisplayName("Set value by reflection on an existing field name")
    public void setValueReflective() {
        final var myObject = new TestObject();

        // try to set on an existing field name
        assertThat(myObject.input1).isNull();
        ReflectHelper.setInternalValue(myObject, "input1", "foo");
        assertThat(myObject.input1).isEqualTo("foo");
    }

    @Test
    @DisplayName("Set value by reflection on a non-existing field name")
    public void setValueReflectiveUnknownFieldName() {
        final var myObject = new TestObject();

        // try to set on an unknown field name
        assertThatThrownBy(() -> ReflectHelper.setInternalValue(myObject, "iDoNotExists", "foo"))
                .isInstanceOf(ReflectException.class)
                .hasMessage("Could not find field name 'iDoNotExists' in class hierarchy of class '%s'", myObject.getClass());
    }

    @Test
    @DisplayName("Set value by reflection on a non-existing field")
    public void setValueReflectiveUnknownField() throws NoSuchFieldException {
        final var declaredField = TestObject.class.getDeclaredField("output");

        // try to set on an non-existing field
        final var wrongObject = new Object();
        assertThatThrownBy(() -> ReflectHelper.setInternalValue(wrongObject, declaredField, "foo"))
                .isInstanceOf(ReflectException.class)
                .hasMessage("Could not set value 'foo' for owner '%s' and field '%s'", wrongObject, declaredField);
    }

    @Test
    @DisplayName("Get value by reflection on an existing field name")
    public void getValueReflective() {
        final var myObject = new TestObject();

        // try to get value from existing field name
        assertThat(myObject.input2).isEqualTo("bar");
        assertThat(ReflectHelper.<String>getInternalValue(myObject, "input2")).isEqualTo("bar");
    }

    @Test
    @DisplayName("Get value by reflection on a non-existing field")
    public void getValueReflectiveUnknownField() throws NoSuchFieldException {
        final var declaredField = TestObject.class.getDeclaredField("output");

        // try to set on an non-existing field
        final var wrongObject = new Object();
        assertThatThrownBy(() -> ReflectHelper.getInternalValue(wrongObject, declaredField))
                .isInstanceOf(ReflectException.class)
                .hasMessage("Could not get value for owner '%s' and field '%s'", wrongObject, declaredField);
    }

    @Test
    @DisplayName("Constructor not instantiable")
    public void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = ReflectHelper.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }

    /**
     * Test object for reflection testing
     */
    private static class TestObject {
        @Input
        private Object input1;

        @Input
        private String input2 = "bar";

        @Output
        private Object output;
    }
}
