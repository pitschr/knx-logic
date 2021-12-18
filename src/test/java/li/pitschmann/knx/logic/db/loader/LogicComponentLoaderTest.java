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

package li.pitschmann.knx.logic.db.loader;

import li.pitschmann.knx.logic.LogicRepository;
import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;
import test.components.LogicD;
import test.components.LogicE;
import test.components.LogicF;
import test.components.LogicG;
import test.components.LogicH;
import test.components.LogicI;
import test.components.LogicJ;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static li.pitschmann.knx.logic.uid.UIDFactory.createUid;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

/**
 * Test case for {@link LogicComponentLoader}
 *
 * @author PITSCHR
 */
class LogicComponentLoaderTest extends BaseDatabaseSuite {

    /**
     * {@link test.components.LogicA}
     * Input:  N/A
     * Output: N/A
     */
    @Test
    @DisplayName("Logic A: No Inputs, No Outputs")
    void testLogicA() {
        executeSqlFile(new File(Sql.Logic.A));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-A"));

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
        executeSqlFile(new File(Sql.Logic.B));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-B"));

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputConnector("i").getUid()).isEqualTo(createUid("uid-connector-logic-B#i"));

        assertThat(logic.getInputPins()).hasSize(1);
        assertThat(logic.getInputPin("i").getUid()).isEqualTo(createUid("uid-pin-logic-B#i"));

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
        executeSqlFile(new File(Sql.Logic.C));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-C"));

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputConnector("i").getUid()).isEqualTo(createUid("uid-connector-logic-C#i"));

        assertThat(logic.getInputPins()).hasSize(1);
        assertThat(logic.getInputPin("i").getUid()).isEqualTo(createUid("uid-pin-logic-C#i"));

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputConnector("o").getUid()).isEqualTo(createUid("uid-connector-logic-C#o"));

        assertThat(logic.getOutputPins()).hasSize(1);
        assertThat(logic.getOutputPin("o").getUid()).isEqualTo(createUid("uid-pin-logic-C#o"));
    }

    /**
     * {@link LogicD}
     * Input:  1 Dynamic (Boolean, Size=2)
     * Output: N/A
     */
    @Test
    @DisplayName("Logic D: 1 Dynamic Input, No Outputs")
    void testLogicD() {
        executeSqlFile(new File(Sql.Logic.D));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-D"));

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputConnector("i").getUid()).isEqualTo(createUid("uid-connector-logic-D#i"));

        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("i[0]").getUid()).isEqualTo(createUid("uid-pin-logic-D#i[0]"));
        assertThat(logic.getInputPin("i[1]").getUid()).isEqualTo(createUid("uid-pin-logic-D#i[1]"));

        assertThat(logic.getOutputConnectors()).isEmpty();
        assertThat(logic.getOutputPins()).isEmpty();
    }

    /**
     * {@link LogicE}
     * Input:  1 Dynamic (Boolean, Size=2)
     * Output: 1 Dynamic (Boolean, Size=2)
     */
    @Test
    @DisplayName("Logic E: 1 Dynamic Input, 1 Dynamic Output")
    void testLogicE() {
        executeSqlFile(new File(Sql.Logic.E));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-E"));

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputConnector("i").getUid()).isEqualTo(createUid("uid-connector-logic-E#i"));

        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("i[0]").getUid()).isEqualTo(createUid("uid-pin-logic-E#i[0]"));
        assertThat(logic.getInputPin("i[1]").getUid()).isEqualTo(createUid("uid-pin-logic-E#i[1]"));

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputConnector("o").getUid()).isEqualTo(createUid("uid-connector-logic-E#o"));

        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("o[0]").getUid()).isEqualTo(createUid("uid-pin-logic-E#o[0]"));
        assertThat(logic.getOutputPin("o[1]").getUid()).isEqualTo(createUid("uid-pin-logic-E#o[1]"));
    }

    /**
     * {@link LogicF}
     * Input:
     * - 1 Static (String)
     * - 1 Dynamic (String, Size=2)
     * Output:
     * - 1 Static (String)
     * - 1 Dynamic (String, Size=3)
     */
    @Test
    @DisplayName("Logic F: 1 Static Input, 1 Dynamic Input, 1 Static Output, 1 Dynamic Output")
    void testLogicF() {
        executeSqlFile(new File(Sql.Logic.F));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-F"));

        assertThat(logic.getInputConnectors()).hasSize(2);
        assertThat(logic.getInputConnector("input").getUid()).isEqualTo(createUid("uid-connector-logic-F#input"));
        assertThat(logic.getInputConnector("inputs").getUid()).isEqualTo(createUid("uid-connector-logic-F#inputs"));

        assertThat(logic.getInputPins()).hasSize(3);
        assertThat(logic.getInputPin("input").getUid()).isEqualTo(createUid("uid-pin-logic-F#input"));
        assertThat(logic.getInputPin("inputs[0]").getUid()).isEqualTo(createUid("uid-pin-logic-F#inputs[0]"));
        assertThat(logic.getInputPin("inputs[1]").getUid()).isEqualTo(createUid("uid-pin-logic-F#inputs[1]"));

        assertThat(logic.getOutputConnectors()).hasSize(2);
        assertThat(logic.getOutputConnector("output").getUid()).isEqualTo(createUid("uid-connector-logic-F#output"));
        assertThat(logic.getOutputConnector("outputs").getUid()).isEqualTo(createUid("uid-connector-logic-F#outputs"));

        assertThat(logic.getOutputPins()).hasSize(4);
        assertThat(logic.getOutputPin("output").getUid()).isEqualTo(createUid("uid-pin-logic-F#output"));
        assertThat(logic.getOutputPin("outputs[0]").getUid()).isEqualTo(createUid("uid-pin-logic-F#outputs[0]"));
        assertThat(logic.getOutputPin("outputs[1]").getUid()).isEqualTo(createUid("uid-pin-logic-F#outputs[1]"));
        assertThat(logic.getOutputPin("outputs[2]").getUid()).isEqualTo(createUid("uid-pin-logic-F#outputs[2]"));
    }

    /**
     * {@link LogicG}
     * Input:  8 Static Inputs  (boolean, byte, char,      double, float, int,     long, short)
     * Output: 9 Static Outputs (Boolean, Byte, Character, Double, Float, Integer, Long, Short, String)
     */
    @Test
    @DisplayName("Logic G: 8 Static Inputs, 9 Static Outputs")
    void testLogicG() {
        executeSqlFile(new File(Sql.Logic.G));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-G"));

        assertThat(logic.getInputConnectors()).hasSize(8);
        assertThat(logic.getInputConnector("inputBooleanPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputBooleanPrimitive"));
        assertThat(logic.getInputConnector("inputBytePrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputBytePrimitive"));
        assertThat(logic.getInputConnector("inputCharacterPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputCharacterPrimitive"));
        assertThat(logic.getInputConnector("inputDoublePrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputDoublePrimitive"));
        assertThat(logic.getInputConnector("inputFloatPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputFloatPrimitive"));
        assertThat(logic.getInputConnector("inputIntegerPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputIntegerPrimitive"));
        assertThat(logic.getInputConnector("inputLongPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputLongPrimitive"));
        assertThat(logic.getInputConnector("inputShortPrimitive").getUid()).isEqualTo(createUid("uid-connector-logic-G#inputShortPrimitive"));

        assertThat(logic.getInputPins()).hasSize(8);
        assertThat(logic.getInputPin("inputBooleanPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputBooleanPrimitive"));
        assertThat(logic.getInputPin("inputBytePrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputBytePrimitive"));
        assertThat(logic.getInputPin("inputCharacterPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputCharacterPrimitive"));
        assertThat(logic.getInputPin("inputDoublePrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputDoublePrimitive"));
        assertThat(logic.getInputPin("inputFloatPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputFloatPrimitive"));
        assertThat(logic.getInputPin("inputIntegerPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputIntegerPrimitive"));
        assertThat(logic.getInputPin("inputLongPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputLongPrimitive"));
        assertThat(logic.getInputPin("inputShortPrimitive").getUid()).isEqualTo(createUid("uid-pin-logic-G#inputShortPrimitive"));

        assertThat(logic.getOutputConnectors()).hasSize(9);
        assertThat(logic.getOutputPins()).hasSize(9);
        assertThat(logic.getOutputPin("outputBooleanObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputBooleanObject"));
        assertThat(logic.getOutputPin("outputByteObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputByteObject"));
        assertThat(logic.getOutputPin("outputCharacterObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputCharacterObject"));
        assertThat(logic.getOutputPin("outputDoubleObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputDoubleObject"));
        assertThat(logic.getOutputPin("outputFloatObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputFloatObject"));
        assertThat(logic.getOutputPin("outputIntegerObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputIntegerObject"));
        assertThat(logic.getOutputPin("outputLongObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputLongObject"));
        assertThat(logic.getOutputPin("outputShortObject").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputShortObject"));
        assertThat(logic.getOutputPin("outputString").getUid()).isEqualTo(createUid("uid-pin-logic-G#outputString"));
    }

    /**
     * {@link LogicH}
     * Input:  8 Dynamic Inputs (Boolean, Byte, Character, Double, Float, Integer, Long, Short)
     * Output: 1 Dynamic Output (String, Size=2)
     */
    @Test
    @DisplayName("Logic H: 8 Dynamic Inputs, 1 Dynamic Output")
    void testLogicH() {
        executeSqlFile(new File(Sql.Logic.H));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-H"));

        assertThat(logic.getInputConnectors()).hasSize(8);
        assertThat(logic.getInputConnector("booleans").getUid()).isEqualTo(createUid("uid-connector-logic-H#booleans"));
        assertThat(logic.getInputConnector("bytes").getUid()).isEqualTo(createUid("uid-connector-logic-H#bytes"));
        assertThat(logic.getInputConnector("chars").getUid()).isEqualTo(createUid("uid-connector-logic-H#chars"));
        assertThat(logic.getInputConnector("doubles").getUid()).isEqualTo(createUid("uid-connector-logic-H#doubles"));
        assertThat(logic.getInputConnector("floats").getUid()).isEqualTo(createUid("uid-connector-logic-H#floats"));
        assertThat(logic.getInputConnector("integers").getUid()).isEqualTo(createUid("uid-connector-logic-H#integers"));
        assertThat(logic.getInputConnector("longs").getUid()).isEqualTo(createUid("uid-connector-logic-H#longs"));
        assertThat(logic.getInputConnector("shorts").getUid()).isEqualTo(createUid("uid-connector-logic-H#shorts"));

        assertThat(logic.getInputPins()).hasSize(16);
        assertThat(logic.getInputPin("booleans[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#booleans[0]"));
        assertThat(logic.getInputPin("booleans[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#booleans[1]"));
        assertThat(logic.getInputPin("bytes[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#bytes[0]"));
        assertThat(logic.getInputPin("bytes[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#bytes[1]"));
        assertThat(logic.getInputPin("chars[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#chars[0]"));
        assertThat(logic.getInputPin("chars[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#chars[1]"));
        assertThat(logic.getInputPin("doubles[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#doubles[0]"));
        assertThat(logic.getInputPin("doubles[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#doubles[1]"));
        assertThat(logic.getInputPin("floats[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#floats[0]"));
        assertThat(logic.getInputPin("floats[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#floats[1]"));
        assertThat(logic.getInputPin("integers[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#integers[0]"));
        assertThat(logic.getInputPin("integers[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#integers[1]"));
        assertThat(logic.getInputPin("longs[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#longs[0]"));
        assertThat(logic.getInputPin("longs[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#longs[1]"));
        assertThat(logic.getInputPin("shorts[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#shorts[0]"));
        assertThat(logic.getInputPin("shorts[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#shorts[1]"));

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputConnector("strings").getUid()).isEqualTo(createUid("uid-connector-logic-H#strings"));

        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("strings[0]").getUid()).isEqualTo(createUid("uid-pin-logic-H#strings[0]"));
        assertThat(logic.getOutputPin("strings[1]").getUid()).isEqualTo(createUid("uid-pin-logic-H#strings[1]"));
    }

    /**
     * {@link LogicI}
     * Input: 2x Static (int, int)
     * Output: 2x Static (String, String)
     */
    @Test
    @DisplayName("Logic I: 2 Static Inputs, 2 Static Outputs")
    void testLogicI() {
        executeSqlFile(new File(Sql.Logic.I));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-I"));

        assertThat(logic.getInputConnectors()).hasSize(2);
        assertThat(logic.getInputConnector("inputFirst").getUid()).isEqualTo(createUid("uid-connector-logic-I#inputFirst"));
        assertThat(logic.getInputConnector("inputSecond").getUid()).isEqualTo(createUid("uid-connector-logic-I#inputSecond"));

        assertThat(logic.getInputPins()).hasSize(2);
        assertThat(logic.getInputPin("inputFirst").getUid()).isEqualTo(createUid("uid-pin-logic-I#inputFirst"));
        assertThat(logic.getInputPin("inputSecond").getUid()).isEqualTo(createUid("uid-pin-logic-I#inputSecond"));

        assertThat(logic.getOutputConnectors()).hasSize(2);
        assertThat(logic.getOutputConnector("outputFirst").getUid()).isEqualTo(createUid("uid-connector-logic-I#outputFirst"));
        assertThat(logic.getOutputConnector("outputSecond").getUid()).isEqualTo(createUid("uid-connector-logic-I#outputSecond"));

        assertThat(logic.getOutputPins()).hasSize(2);
        assertThat(logic.getOutputPin("outputFirst").getUid()).isEqualTo(createUid("uid-pin-logic-I#outputFirst"));
        assertThat(logic.getOutputPin("outputSecond").getUid()).isEqualTo(createUid("uid-pin-logic-I#outputSecond"));
    }

    /**
     * Tests the loading of {@link LogicJ} component
     * Input: 2x Dynamic (Integer, Size=3 / Integer, Size=2)</li>
     * Output: 2x Dynamic (String, Size=4 / String, Size=2)</li>
     */
    @Test
    @DisplayName("Logic J: 2 Dynamic Inputs, 2 Dynamic Outputs")
    void testLogicJ() {
        executeSqlFile(new File(Sql.Logic.J));

        final var logic = new LogicComponentLoader(databaseManager, mock(LogicRepository.class))
                .loadById(1);

        assertThat(logic.getUid()).isEqualTo(createUid("uid-component-logic-J"));

        assertThat(logic.getInputConnectors()).hasSize(2);
        assertThat(logic.getInputConnector("inputFirst").getUid()).isEqualTo(createUid("uid-connector-logic-J#inputFirst"));
        assertThat(logic.getInputConnector("inputSecond").getUid()).isEqualTo(createUid("uid-connector-logic-J#inputSecond"));

        assertThat(logic.getInputPins()).hasSize(5);
        assertThat(logic.getInputPin("inputFirst[0]").getUid()).isEqualTo(createUid("uid-pin-logic-J#inputFirst[0]"));
        assertThat(logic.getInputPin("inputFirst[1]").getUid()).isEqualTo(createUid("uid-pin-logic-J#inputFirst[1]"));
        assertThat(logic.getInputPin("inputFirst[2]").getUid()).isEqualTo(createUid("uid-pin-logic-J#inputFirst[2]"));
        assertThat(logic.getInputPin("inputSecond[0]").getUid()).isEqualTo(createUid("uid-pin-logic-J#inputSecond[0]"));
        assertThat(logic.getInputPin("inputSecond[1]").getUid()).isEqualTo(createUid("uid-pin-logic-J#inputSecond[1]"));

        assertThat(logic.getOutputConnectors()).hasSize(2);
        assertThat(logic.getOutputConnector("outputFirst").getUid()).isEqualTo(createUid("uid-connector-logic-J#outputFirst"));
        assertThat(logic.getOutputConnector("outputSecond").getUid()).isEqualTo(createUid("uid-connector-logic-J#outputSecond"));

        assertThat(logic.getOutputPins()).hasSize(6);
        assertThat(logic.getOutputPin("outputFirst[0]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputFirst[0]"));
        assertThat(logic.getOutputPin("outputFirst[1]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputFirst[1]"));
        assertThat(logic.getOutputPin("outputFirst[2]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputFirst[2]"));
        assertThat(logic.getOutputPin("outputFirst[3]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputFirst[3]"));
        assertThat(logic.getOutputPin("outputSecond[0]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputSecond[0]"));
        assertThat(logic.getOutputPin("outputSecond[1]").getUid()).isEqualTo(createUid("uid-pin-logic-J#outputSecond[1]"));
    }

    @Test
    @DisplayName("Logic (JAR): JAR file not loaded/scanned yet, MyFooBarLogic is not present")
    void testLogicFooBar_NotLoadedYet() {
        executeSqlFile(new File(Sql.Logic.JAR_FOOBAR));

        final var loader = new LogicComponentLoader(databaseManager, new LogicRepository());

        assertThatThrownBy(() -> loader.loadById(1))
                .isInstanceOf(NoLogicClassFound.class)
                .hasMessage("No Logic Class found: my.logic.MyFooBarLogic");
    }

    @Test
    @DisplayName("Logic (JAR): JAR file scanned, MyFooBarLogic is present")
    void testLogicFooBar_Loaded() throws IOException {
        executeSqlFile(new File(Sql.Logic.JAR_FOOBAR));

        final var logicRepository = new LogicRepository();
        final var loader = new LogicComponentLoader(databaseManager, logicRepository);

        // Step 1: load from JAR
        logicRepository.scanLogicClasses(Paths.get("."));

        // Step 2: Load from database and wrap the logic from JAR file
        final var logic = loader.loadById(1);

        assertThat(logic.getWrappedObject().getClass().getName()).isEqualTo("my.logic.MyFooBarLogic");

        assertThat(logic.getInputConnectors()).hasSize(1);
        assertThat(logic.getInputPins()).hasSize(1);
        assertThat(logic.getInputPin("inputText")).isNotNull();

        assertThat(logic.getOutputConnectors()).hasSize(1);
        assertThat(logic.getOutputPins()).hasSize(1);
        assertThat(logic.getOutputPin("outputText")).isNotNull();
    }
}
