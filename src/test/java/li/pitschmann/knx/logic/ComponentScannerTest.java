package li.pitschmann.knx.logic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test {@link ComponentScanner}
 */
public class ComponentScannerTest {

    @Test
    @DisplayName("Get class from JAR file that implements Logic interface")
    public void findLogicImplementation() throws Throwable {
        // JAR contains two classes:
        // -> MyFooBarLogic and
        // -> EmptyObject (will be ignored as it is not a logic)
        final var jarPath = Paths.get(getClass().getResource("/components/foobar-logic.jar").toURI());

        final var allLogicClasses = ComponentScanner.scanByFileOrFolder(jarPath, Logic.class);
        assertThat(allLogicClasses).hasSize(1);
        assertThat(allLogicClasses.get(0).getName()).isEqualTo("my.logic.MyFooBarLogic");
    }

    @Test
    @DisplayName("Get all classes from JAR file that implements Logic interface")
    public void findAllLogicFromJAR() throws Throwable {
        final var jarPath = Paths.get(getClass().getResource("/components/core-logic.jar").toURI());

        final var allLogicClasses = ComponentScanner.scanByFileOrFolder(jarPath, Logic.class);
        final var allLogicClassNames = allLogicClasses.stream().map(Class::getName).toArray();
        assertThat(allLogicClassNames).containsExactly(
                "my.logic.bitwise.CoreLogicA",
                "my.logic.bitwise.CoreLogicB",
                "my.logic.general.CoreLogicC",
                "my.logic.subA.subB.CoreLogicD",
                "my.logic.text.CoreLogicE"
        );
    }

    @Test
    @DisplayName("Get all classes from folder that implements Logic interface")
    public void findAllLogicFromFolder() throws Throwable {
        final var folderPath = Paths.get(getClass().getResource("/components/").toURI());

        final var allLogicClasses = ComponentScanner.scanByFileOrFolder(folderPath, Logic.class);
        final var allLogicClassNames = allLogicClasses.stream().map(Class::getName).toArray();
        assertThat(allLogicClassNames).containsExactlyInAnyOrder(
                // from '/components/core-logic.jar'
                "my.logic.bitwise.CoreLogicA",
                "my.logic.bitwise.CoreLogicB",
                "my.logic.general.CoreLogicC",
                "my.logic.subA.subB.CoreLogicD",
                "my.logic.text.CoreLogicE",
                // from '/components/foobar-logic.jar'
                "my.logic.MyFooBarLogic"
        );
    }

    @Test
    @DisplayName("ERROR: Try to load a class that has wrong file name")
    public void findInvalidLogicImplementation() throws Throwable {
        final var jarPath = Paths.get(getClass().getResource("/components/wrong-logic-name.jar").toURI());

        final var allLogicClasses = ComponentScanner.scanByFileOrFolder(jarPath, Logic.class);
        // expected is that it silently returns file but do not throw an exception
        assertThat(allLogicClasses).isEmpty();
    }

    @Test
    @DisplayName("Constructor not instantiable")
    public void testConstructorNonInstantiable() {
        assertThatThrownBy(() -> {
            final var ctor = ComponentScanner.class.getDeclaredConstructor();
            ctor.trySetAccessible();
            ctor.newInstance();
        }).hasCauseInstanceOf(AssertionError.class);
    }
}
