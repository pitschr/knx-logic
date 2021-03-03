package li.pitschmann.knx.logic.transformers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test for {@link Transformers}
 */
public class TransformersTest {

    @Test
    @DisplayName("Unsupported Transformers")
    void testUnknownTransformer() {
        assertThatThrownBy(() -> Transformers.transform("", null))
                .isInstanceOf(NullPointerException.class);

        assertThatThrownBy(() -> Transformers.transform("", boolean.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("No transformation for primitive classes. Use wrapper classes.");

        assertThatThrownBy(() -> Transformers.transform("", Class.class))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find a matching transformer for: java.lang.Class");

    }

    @Test
    @DisplayName("Transform value to 'Boolean'")
    void testBoolean() {
        // empty + null -> false
        assertThat(Transformers.transform("", Boolean.class)).isFalse();
        assertThat(Transformers.transform(null, Boolean.class)).isFalse();

        // true
        assertThat(Transformers.transform("1", Boolean.class)).isTrue();
        assertThat(Transformers.transform("true", Boolean.class)).isTrue();
        assertThat(Transformers.transform("True", Boolean.class)).isTrue();
        assertThat(Transformers.transform("TRUE", Boolean.class)).isTrue();

        // false
        assertThat(Transformers.transform("0", Boolean.class)).isFalse();
        assertThat(Transformers.transform("false", Boolean.class)).isFalse();
        assertThat(Transformers.transform("False", Boolean.class)).isFalse();
        assertThat(Transformers.transform("FALSE", Boolean.class)).isFalse();

        // non-supported conversion
        assertThat(Transformers.transform("foobar", Boolean.class)).isFalse();
    }

    @Test
    @DisplayName("Transform value to 'Byte'")
    void testByte() {
        // empty + null -> false
        assertThat(Transformers.transform("", Byte.class)).isZero();
        assertThat(Transformers.transform(null, Byte.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Byte.class)).isEqualTo((byte) 0);
        assertThat(Transformers.transform("1", Byte.class)).isEqualTo((byte) 1);
        assertThat(Transformers.transform("-1", Byte.class)).isEqualTo((byte) -1);
        assertThat(Transformers.transform("127", Byte.class)).isEqualTo((byte) 127);
        assertThat(Transformers.transform("-128", Byte.class)).isEqualTo((byte) -128);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("0x00", Byte.class))
                .isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> Transformers.transform("lorem ipsum", Byte.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'Character'")
    void testCharacter() {
        // empty + null -> false
        assertThat(Transformers.transform("", Character.class)).isSameAs(Character.MIN_VALUE);
        assertThat(Transformers.transform(null, Character.class)).isSameAs(Character.MIN_VALUE);

        // supported conversion
        assertThat(Transformers.transform("a", Character.class)).isEqualTo('a');
        assertThat(Transformers.transform("b", Character.class)).isEqualTo('b');
        assertThat(Transformers.transform("ä", Character.class)).isEqualTo('ä');
        assertThat(Transformers.transform("0", Character.class)).isEqualTo('0');
        assertThat(Transformers.transform("1", Character.class)).isEqualTo('1');
        assertThat(Transformers.transform("\\u0040", Character.class)).isEqualTo('@');
        assertThat(Transformers.transform("\\u00C4", Character.class)).isEqualTo('Ä');

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("abc", Character.class))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Transform value to 'Double'")
    void testDouble() {
        // empty + null -> false
        assertThat(Transformers.transform("", Double.class)).isZero();
        assertThat(Transformers.transform(null, Double.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Double.class)).isEqualTo(0d);
        assertThat(Transformers.transform("1", Double.class)).isEqualTo(1d);
        assertThat(Transformers.transform("-1", Double.class)).isEqualTo(-1d);
        assertThat(Transformers.transform("123.45678", Double.class)).isEqualTo(123.45678d);
        assertThat(Transformers.transform("-234.56789", Double.class)).isEqualTo(-234.56789d);
        assertThat(Transformers.transform("1.234E6", Double.class)).isEqualTo(1234000d);
        assertThat(Transformers.transform("2.34567890123456E4", Double.class)).isEqualTo(23456.7890123456d);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("dolor", Double.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'Float'")
    void testFloat() {
        // empty + null -> false
        assertThat(Transformers.transform("", Float.class)).isZero();
        assertThat(Transformers.transform(null, Float.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Float.class)).isEqualTo(0f);
        assertThat(Transformers.transform("1", Float.class)).isEqualTo(1f);
        assertThat(Transformers.transform("-1", Float.class)).isEqualTo(-1f);
        assertThat(Transformers.transform("123.45678", Float.class)).isEqualTo(123.45678f);
        assertThat(Transformers.transform("-234.56789", Float.class)).isEqualTo(-234.56789f);
        assertThat(Transformers.transform("1.234E6", Float.class)).isEqualTo(1234000f);
        assertThat(Transformers.transform("2.34567890123456E4", Float.class)).isEqualTo(23456.7890123456f);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("sit amet", Float.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'Integer'")
    void testInteger() {
        // empty + null -> false
        assertThat(Transformers.transform("", Integer.class)).isZero();
        assertThat(Transformers.transform(null, Integer.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Integer.class)).isEqualTo(0);
        assertThat(Transformers.transform("1", Integer.class)).isEqualTo(1);
        assertThat(Transformers.transform("-1", Integer.class)).isEqualTo(-1);
        assertThat(Transformers.transform("2147483647", Integer.class)).isEqualTo(Integer.MAX_VALUE);
        assertThat(Transformers.transform("-2147483648", Integer.class)).isEqualTo(Integer.MIN_VALUE);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("omnis", Integer.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'Long'")
    void testLong() {
        // empty + null -> false
        assertThat(Transformers.transform("", Long.class)).isZero();
        assertThat(Transformers.transform(null, Long.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Long.class)).isEqualTo(0L);
        assertThat(Transformers.transform("1", Long.class)).isEqualTo(1L);
        assertThat(Transformers.transform("-1", Long.class)).isEqualTo(-1L);
        assertThat(Transformers.transform("9223372036854775807", Long.class)).isEqualTo(Long.MAX_VALUE);
        assertThat(Transformers.transform("-9223372036854775808", Long.class)).isEqualTo(Long.MIN_VALUE);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("set diam", Long.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'Short'")
    void testShort() {
        // empty + null -> false
        assertThat(Transformers.transform("", Short.class)).isZero();
        assertThat(Transformers.transform(null, Short.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", Short.class)).isEqualTo((short) 0);
        assertThat(Transformers.transform("1", Short.class)).isEqualTo((short) 1);
        assertThat(Transformers.transform("-1", Short.class)).isEqualTo((short) -1);
        assertThat(Transformers.transform("32767", Short.class)).isEqualTo(Short.MAX_VALUE);
        assertThat(Transformers.transform("-32768", Short.class)).isEqualTo(Short.MIN_VALUE);

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("nonumy", Short.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'String'")
    void testString() {
        // empty + null -> false
        assertThat(Transformers.transform("", String.class)).isEmpty();
        assertThat(Transformers.transform(null, String.class)).isEmpty();

        // supported conversion
        assertThat(Transformers.transform("0", String.class)).isEqualTo("0");
        assertThat(Transformers.transform("1", String.class)).isEqualTo("1");
        assertThat(Transformers.transform("-1", String.class)).isEqualTo("-1");
        assertThat(Transformers.transform("abc", String.class)).isEqualTo("abc");
        assertThat(Transformers.transform("xyz", String.class)).isEqualTo("xyz");
    }

    @Test
    @DisplayName("Transform value to 'BigInteger'")
    void testBigInteger() {
        // empty + null -> false
        assertThat(Transformers.transform("", BigInteger.class)).isZero();
        assertThat(Transformers.transform(null, BigInteger.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", BigInteger.class)).isZero();
        assertThat(Transformers.transform("1", BigInteger.class)).isOne();
        assertThat(Transformers.transform("-1", BigInteger.class)).isEqualTo(BigInteger.valueOf(-1));
        assertThat(Transformers.transform("123456789012345678901234567890", BigInteger.class))
                .isEqualTo(new BigInteger("123456789012345678901234567890"));
        assertThat(Transformers.transform("-987654321098765432109876543210", BigInteger.class))
                .isEqualTo(new BigInteger("-987654321098765432109876543210"));

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("fooBigInteger", BigInteger.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Transform value to 'BigDecimal'")
    void testBigDecimal() {
        // empty + null -> false
        assertThat(Transformers.transform("", BigDecimal.class)).isZero();
        assertThat(Transformers.transform(null, BigDecimal.class)).isZero();

        // supported conversion
        assertThat(Transformers.transform("0", BigDecimal.class)).isZero();
        assertThat(Transformers.transform("1.23456789", BigDecimal.class)).isEqualTo(BigDecimal.valueOf(1.23456789d));
        assertThat(Transformers.transform("-9.87654321", BigDecimal.class)).isEqualTo(BigDecimal.valueOf(-9.87654321d));
        assertThat(Transformers.transform("5678901234567890123456789012345.67890123456789", BigDecimal.class))
                .isEqualTo(new BigDecimal("5678901234567890123456789012345.67890123456789"));
        assertThat(Transformers.transform("-432109876543210987654321098765.432109876543210", BigDecimal.class))
                .isEqualTo(new BigDecimal("-432109876543210987654321098765.432109876543210"));

        // non-supported conversion
        assertThatThrownBy(() -> Transformers.transform("fooBigDecimal", BigDecimal.class))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Constructor not instantiable")
    void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = Transformers.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }
}
