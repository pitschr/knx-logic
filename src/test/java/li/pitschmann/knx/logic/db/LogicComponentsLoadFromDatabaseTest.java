package li.pitschmann.knx.logic.db;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for loading {@link li.pitschmann.knx.logic.components.LogicComponent} from database
 *
 * @author PITSCHR
 */
class LogicComponentsLoadFromDatabaseTest extends BaseDatabaseSuite {

    @Override
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        databaseManager.executeSqlFile(new File(Sql.INSERT_LOGIC_COMPONENT_SAMPLES));
        assertThat(componentsDao().size()).isEqualTo(9);
        assertThat(connectorsDao().size()).isEqualTo(40);
        assertThat(pinsDao().size()).isEqualTo(59);
    }

    /**
     * {@link test.components.LogicA}
     * Input:  N/A
     * Output: N/A
     */
    @Test
    @DisplayName("Logic A: No Inputs, No Outputs")
    void testLogicA() {
        final var logic = objectsDao().getLogicComponentById(1);

        assertThat(logic.getInputConnectors()).isEmpty();
        assertThat(logic.getInputPins()).isEmpty();

        assertThat(logic.getOutputConnectors()).isEmpty();
        assertThat(logic.getOutputPins()).isEmpty();
    }

    /**
     * {@link test.components.LogicB}
     * Input:  1 Static (Boolean)
     * Output: N/A
     */
    @Test
    @DisplayName("Logic B: 1 Static Input, No Outputs")
    void testLogicB() {
        final var logic = objectsDao().getLogicComponentById(2);

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputPins()).hasSize(1);
        assertThat(logic.getInputPin("i").getValue()).isEqualTo(true);

        assertThat(logic.getOutputConnectors()).isEmpty();
        assertThat(logic.getOutputPins()).isEmpty();
    }

    /**
     * {@link test.components.LogicC}
     * Input:  1 Static (Boolean)
     * Output: 1 Static (Boolean)
     */
    @Test
    @DisplayName("Logic C: 1 Static Input, 1 Static Output")
    void testLogicC() {
        final var logic = objectsDao().getLogicComponentById(3);

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputPins()).hasSize(1);
        assertThat(logic.getInputPin("i").getValue()).isEqualTo(true);

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputPins()).hasSize(1);
        assertThat(logic.getOutputPin("o").getValue()).isEqualTo(false);
    }

    /**
     * {@link test.components.LogicE}
     * Input:  1 Dynamic (Boolean, Size=2)
     * Output: N/A
     */
    @Test
    @DisplayName("Logic E: 1 Dynamic Input, No Outputs")
    void testLogicE() {
        final var logic = objectsDao().getLogicComponentById(4);

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("i[0]").getValue()).isEqualTo(true);
        assertThat(logic.getInputPin("i[1]").getValue()).isEqualTo(false);

        assertThat(logic.getOutputConnectors()).isEmpty();
        assertThat(logic.getOutputPins()).isEmpty();
    }

    /**
     * {@link test.components.LogicF}
     * Input:  1 Dynamic (Boolean, Size=2)
     * Output: 1 Dynamic (Boolean, Size=2)
     */
    @Test
    @DisplayName("Logic F: 1 Dynamic Input, 1 Dynamic Output")
    void testLogicF() {
        final var logic = objectsDao().getLogicComponentById(5);

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("i[0]").getValue()).isEqualTo(true);
        assertThat(logic.getInputPin("i[1]").getValue()).isEqualTo(false);

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("o[0]").getValue()).isEqualTo(false);
        assertThat(logic.getOutputPin("o[1]").getValue()).isEqualTo(true);
    }

    /**
     * {@link test.components.LogicH}
     * Input:  8 Static Inputs  (boolean, byte, char,      double, float, int,     long, short)
     * Output: 9 Static Outputs (Boolean, Byte, Character, Double, Float, Integer, Long, Short, String)
     */
    @Test
    @DisplayName("Logic H: 8 Static Inputs, 9 Static Outputs")
    void testLogicH() {
        final var logic = objectsDao().getLogicComponentById(6);

        assertThat(logic.getInputConnectors()).hasSize(8);
        assertThat(logic.getInputPins()).hasSize(8);
        assertThat(logic.getInputPin("inputBooleanPrimitive").getValue()).isEqualTo(true);
        assertThat(logic.getInputPin("inputBytePrimitive").getValue()).isEqualTo(Byte.MAX_VALUE);
        assertThat(logic.getInputPin("inputCharacterPrimitive").getValue()).isEqualTo(Character.MAX_VALUE);
        assertThat(logic.getInputPin("inputDoublePrimitive").getValue()).isEqualTo(Double.MAX_VALUE);
        assertThat(logic.getInputPin("inputFloatPrimitive").getValue()).isEqualTo(Float.MAX_VALUE);
        assertThat(logic.getInputPin("inputIntegerPrimitive").getValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(logic.getInputPin("inputLongPrimitive").getValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(logic.getInputPin("inputShortPrimitive").getValue()).isEqualTo(Short.MAX_VALUE);

        assertThat(logic.getOutputConnectors()).hasSize(9);
        assertThat(logic.getOutputPins()).hasSize(9);
        assertThat(logic.getOutputPin("outputBooleanObject").getValue()).isEqualTo(Boolean.FALSE);
        assertThat(logic.getOutputPin("outputByteObject").getValue()).isEqualTo(Byte.valueOf(Byte.MIN_VALUE));
        assertThat(logic.getOutputPin("outputCharacterObject").getValue()).isEqualTo(Character.valueOf(Character.MIN_VALUE));
        assertThat(logic.getOutputPin("outputDoubleObject").getValue()).isEqualTo(Double.valueOf(Double.MIN_VALUE));
        assertThat(logic.getOutputPin("outputFloatObject").getValue()).isEqualTo(Float.valueOf(Float.MIN_VALUE));
        assertThat(logic.getOutputPin("outputIntegerObject").getValue()).isEqualTo(Integer.valueOf(Integer.MIN_VALUE));
        assertThat(logic.getOutputPin("outputLongObject").getValue()).isEqualTo(Long.valueOf(Long.MIN_VALUE));
        assertThat(logic.getOutputPin("outputShortObject").getValue()).isEqualTo(Short.valueOf(Short.MIN_VALUE));
        assertThat(logic.getOutputPin("outputString").getValue()).isEqualTo("Lorem Ipsum");
    }

    /**
     * {@link test.components.LogicI}
     * Input:  8 Dynamic Inputs (Boolean, Byte, Character, Double, Float, Integer, Long, Short)
     * Output: 1 Dynamic Output (String, Size=2)
     */
    @Test
    @DisplayName("Logic I: 8 Dynamic Inputs, 1 Dynamic Output")
    void testLogicI() {
        final var logic = objectsDao().getLogicComponentById(7);

        assertThat(logic.getInputConnectors()).hasSize(8);
        assertThat(logic.getInputPins()).hasSize(16);
        assertThat(logic.getInputPin("booleans[0]").getValue()).isEqualTo(Boolean.FALSE);
        assertThat(logic.getInputPin("booleans[1]").getValue()).isEqualTo(Boolean.TRUE);
        assertThat(logic.getInputPin("bytes[0]").getValue()).isEqualTo(Byte.MIN_VALUE);
        assertThat(logic.getInputPin("bytes[1]").getValue()).isEqualTo(Byte.MAX_VALUE);
        assertThat(logic.getInputPin("chars[0]").getValue()).isEqualTo(Character.MIN_VALUE);
        assertThat(logic.getInputPin("chars[1]").getValue()).isEqualTo(Character.MAX_VALUE);
        assertThat(logic.getInputPin("doubles[0]").getValue()).isEqualTo(Double.MIN_VALUE);
        assertThat(logic.getInputPin("doubles[1]").getValue()).isEqualTo(Double.MAX_VALUE);
        assertThat(logic.getInputPin("floats[0]").getValue()).isEqualTo(Float.MIN_VALUE);
        assertThat(logic.getInputPin("floats[1]").getValue()).isEqualTo(Float.MAX_VALUE);
        assertThat(logic.getInputPin("integers[0]").getValue()).isEqualTo(Integer.MIN_VALUE);
        assertThat(logic.getInputPin("integers[1]").getValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(logic.getInputPin("longs[0]").getValue()).isEqualTo(Long.MIN_VALUE);
        assertThat(logic.getInputPin("longs[1]").getValue()).isEqualTo(Long.MAX_VALUE);
        assertThat(logic.getInputPin("shorts[0]").getValue()).isEqualTo(Short.MIN_VALUE);
        assertThat(logic.getInputPin("shorts[1]").getValue()).isEqualTo(Short.MAX_VALUE);

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("strings[0]").getValue()).isEqualTo("Lorem Ipsum");
        assertThat(logic.getOutputPin("strings[1]").getValue()).isEqualTo("Dolor Sit Amet");
    }

    /**
     * {@link test.components.LogicJ}
     * Input: 2x Static (int, int)
     * Output: 2x Static (String, String)
     */
    @Test
    @DisplayName("Logic J: 2 Static Inputs, 2 Static Outputs")
    void testLogicJ() {
        final var logic = objectsDao().getLogicComponentById(8);

        assertThat(logic.getInputConnectors()).hasSize(2);
        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("inputFirst").getValue()).isEqualTo(4711);
        assertThat(logic.getInputPin("inputSecond").getValue()).isEqualTo(13);

        assertThat(logic.getOutputConnectors()).hasSize(2);
        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("outputFirst").getValue()).isEqualTo("Hello");
        assertThat(logic.getOutputPin("outputSecond").getValue()).isEqualTo("World");
    }

    /**
     * Tests the loading of {@link test.components.LogicK} component
     * Input: 2x Dynamic (Integer, Size=3 / Integer, Size=2)</li>
     * Output: 2x Dynamic (String, Size=4 / String, Size=2)</li>
     */
    @Test
    @DisplayName("Logic K: 2 Dynamic Inputs, 2 Dynamic Outputs")
    void testLogicK() {
        final var logic = objectsDao().getLogicComponentById(9);

        assertThat(logic.getInputConnectors()).hasSize(2);
        assertThat(logic.getInputPins()).hasSize(5);
        assertThat(logic.getInputPin("inputFirst[0]").getValue()).isEqualTo(1024);
        assertThat(logic.getInputPin("inputFirst[1]").getValue()).isEqualTo(4201);
        assertThat(logic.getInputPin("inputFirst[2]").getValue()).isEqualTo(2014);
        assertThat(logic.getInputPin("inputSecond[0]").getValue()).isEqualTo(17);
        assertThat(logic.getInputPin("inputSecond[1]").getValue()).isEqualTo(71);

        assertThat(logic.getOutputConnectors()).hasSize(2);
        assertThat(logic.getOutputPins()).hasSize(6);
        assertThat(logic.getOutputPin("outputFirst[0]").getValue()).isEqualTo("Hello");
        assertThat(logic.getOutputPin("outputFirst[1]").getValue()).isEqualTo("olleH");
        assertThat(logic.getOutputPin("outputFirst[2]").getValue()).isEqualTo("lloeH");
        assertThat(logic.getOutputPin("outputFirst[3]").getValue()).isEqualTo("Hoell");
        assertThat(logic.getOutputPin("outputSecond[0]").getValue()).isEqualTo("World");
        assertThat(logic.getOutputPin("outputSecond[1]").getValue()).isEqualTo("dlroW");
    }
}
