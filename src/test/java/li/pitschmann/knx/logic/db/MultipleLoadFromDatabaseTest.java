package li.pitschmann.knx.logic.db;

import org.junit.jupiter.api.RepeatedTest;
import test.BaseDatabaseSuite;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test case for loading components concurrently/sequentially from database
 *
 * @author PITSCHR
 */
class MultipleLoadFromDatabaseTest extends BaseDatabaseSuite {
    private static final int TIMES = 100;

    @Override
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        databaseManager.executeSqlFile(new File(Sql.LOAD_SEQUENTIALLY_TEST));
        assertThat(componentsDao().size()).isEqualTo(1);
        assertThat(connectorsDao().size()).isEqualTo(2);
        assertThat(pinsDao().size()).isEqualTo(2);
    }

    /**
     * Tests the <strong>sequential</strong> loading of same logic component (in this example the
     * {@link ComponentId#LOGIC_C} component) multiple times. First and last repetition should be ignored because
     * of database warm up and warm down.
     */
    @RepeatedTest(10)
    void loadSequentially() {
        for (int i = 0; i < TIMES; i++) {
            final var logic = objectsDao().getLogicComponentById(1);
            assertThat(logic).isNotNull();
        }
    }
}
