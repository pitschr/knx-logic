package li.pitschmann.knx.logic;

import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test {@link LogicRepository}
 */
class LogicRepositoryTest {

    @Test
    @DisplayName("Empty Logic Repository")
    void testInitLogicRepository() {
        final var repository = new LogicRepository();

        assertThat(repository.getAllLogicClasses()).isEmpty();
        assertThatThrownBy(() -> repository.findLogicClass("my.logic.NonExistentLogic"))
                .isInstanceOf(NoLogicClassFound.class)
                .hasMessage("The logic class could not be found: my.logic.NonExistentLogic");
    }

    @Test
    @DisplayName("Scan for Logic class in one JAR file")
    void scanForOneJarPathAndFind() throws Throwable {
        final var jarPath = Paths.get(getClass().getResource("/components/foobar-logic.jar").toURI());

        final var repository = new LogicRepository();
        repository.scanLogicClasses(jarPath);

        // should only one entry
        assertThat(repository.getAllLogicClasses()).hasSize(1);
        assertThat(repository.getAllLogicClasses().get(0).getName()).isEqualTo("my.logic.MyFooBarLogic");

        // when finding
        final var clazz = repository.findLogicClass("my.logic.MyFooBarLogic");
        assertThat(clazz).isNotNull();
        assertThat(clazz.getName()).isEqualTo("my.logic.MyFooBarLogic");
    }

    @Test
    @DisplayName("Scan for Logic class in entire folder")
    void scanForOneFolderPathAndFind() throws Throwable {
        final var folderPath = Paths.get(".");

        final var repository = new LogicRepository();
        repository.scanLogicClasses(folderPath);

        // should be multiple entries
        assertThat(repository.getAllLogicClasses()).hasSize(6);

        // try finding it
        assertThat(repository.findLogicClass("my.logic.MyFooBarLogic")).isNotNull();
        assertThat(repository.findLogicClass("my.logic.bitwise.CoreLogicA")).isNotNull();
    }

}
