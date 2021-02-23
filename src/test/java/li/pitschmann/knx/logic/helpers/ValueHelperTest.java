package li.pitschmann.knx.logic.helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link ValueHelper}
 */
class ValueHelperTest {

    @Test
    @DisplayName("Get default values")
    void getDefaultValues() {
        // objects
        assertThat(ValueHelper.getDefaultValueFor(Boolean.class)).isFalse();
        assertThat(ValueHelper.getDefaultValueFor(Short.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Integer.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Long.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Float.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Double.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Byte.class)).isZero();
        assertThat(ValueHelper.getDefaultValueFor(Character.class)).isSameAs('\u0000');
        assertThat(ValueHelper.getDefaultValueFor(String.class)).isEmpty();

        // special case for Number
        assertThat(ValueHelper.getDefaultValueFor(Number.class)).isEqualTo(0);

        // for other class types
        assertThat(ValueHelper.getDefaultValueFor(Object.class)).isNull();
        assertThat(ValueHelper.getDefaultValueFor(Exception.class)).isNull();

        // not supported class types
        assertThatThrownBy(() -> ValueHelper.getDefaultValueFor(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("No class provided");
        assertThatThrownBy(() -> ValueHelper.getDefaultValueFor(boolean.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Class should not be a primitive");
    }

    @Test
    @DisplayName("Get value type of field")
    void testValueType() throws NoSuchFieldException {
        // int
        final var primitiveInt = TestObject.class.getDeclaredField("primitiveInt");
        assertThat(ValueHelper.getFieldType(primitiveInt)).isSameAs(Integer.class);

        // Integer
        final var objectInt = TestObject.class.getDeclaredField("objectInt");
        assertThat(ValueHelper.getFieldType(objectInt)).isSameAs(Integer.class);

        // List<Integer>
        final var listInt = TestObject.class.getDeclaredField("listInt");
        assertThat(ValueHelper.getFieldType(listInt)).isSameAs(Integer.class);

        // List<?>
        final var wildcard = TestObject.class.getDeclaredField("wildcard");
        assertThatThrownBy(() -> ValueHelper.getFieldType(wildcard))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("No value class found for field '%s' and index '0'", wildcard);

        // <T>
        final var objGeneric = TestObjectWithGeneric.class.getDeclaredField("obj");
        assertThat(ValueHelper.getFieldType(objGeneric)).isSameAs(Object.class);

        // List<T>
        final var listGeneric = TestObjectWithGeneric.class.getDeclaredField("list");
        assertThat(ValueHelper.getFieldType(listGeneric)).isSameAs(Object.class);

        // <T extends Number>
        final var objNumberGeneric = TestObjectWithNumberGeneric.class.getDeclaredField("obj");
        assertThat(ValueHelper.getFieldType(objNumberGeneric)).isSameAs(Number.class);

        // List<T extends Number>
        final var listNumberGeneric = TestObjectWithNumberGeneric.class.getDeclaredField("list");
        assertThat(ValueHelper.getFieldType(listNumberGeneric)).isSameAs(Number.class);
    }

    @Test
    @DisplayName("Constructor not instantiable")
    void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = ValueHelper.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }

    /**
     * Test instance to get value type from field
     */
    private static class TestObject {
        private int primitiveInt;
        private Integer objectInt;
        private List<Integer> listInt;
        private List<?> wildcard;
    }

    private static class TestObjectWithGeneric<T> {
        private T obj;
        private List<T> list;
    }

    private static class TestObjectWithNumberGeneric<T extends Number> {
        private T obj;
        private List<T> list;
    }
}
