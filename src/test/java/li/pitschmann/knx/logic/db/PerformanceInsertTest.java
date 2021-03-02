package li.pitschmann.knx.logic.db;

import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import test.BaseDatabaseSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Database Performance Test for INSERTS (difference between each query, handle and batch)
 */
@Disabled
class PerformanceInsertTest extends BaseDatabaseSuite {
    private static final int NUMBER_OF_ITERATIONS = 1000;
    private static final int NUMBER_OF_REPEATED_TESTS = 5;
    private static final int NUMBER_OF_WARM_UP_ITERATIONS = 3;
    private static final String QUERY = "INSERT INTO performance (text) VALUES ('Performance #%s');";
    private static final List<String> QUERIES = new ArrayList<>(NUMBER_OF_ITERATIONS);
    private static PerformanceDao performanceDao;

    static {
        IntStream.range(0, NUMBER_OF_ITERATIONS).mapToObj(i -> String.format(QUERY, i)).forEach(QUERIES::add);
    }

    @Override
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        // register dao
        performanceDao = dao(PerformanceDao.class);

        // Prepare test
        databaseManager.executeSqlFile(new File(Sql.General.PERFORMANCE_TEST));
        assertThat(performanceDao.size()).isZero();

        // perform a warm-up
        for (var i = 0; i < NUMBER_OF_WARM_UP_ITERATIONS; i++) {
            testSqlQueryPerformance();             // test method #1
            testHandlePerformance();               // test method #2
            testBatchPerformance();                // test method #3
            testDaoWithForEachInsertPerformance(); // test method #4
            testDaoWithInsertBatchPerformance();   // test method #5
        }

        // size should be 3 warm-up iterations for 5 test methods and 1000 iterations per test method
        final var totalSizeWarmUp = NUMBER_OF_WARM_UP_ITERATIONS * 5 * NUMBER_OF_ITERATIONS;
        assertThat(performanceDao.size()).isEqualTo(totalSizeWarmUp);
    }

    @Override
    public void beforeDatabaseStop(final DatabaseManager databaseManager) {
        final var totalSizeWarmUp = NUMBER_OF_WARM_UP_ITERATIONS * 5 * NUMBER_OF_ITERATIONS;
        assertThat(performanceDao.size()).isEqualTo(totalSizeWarmUp + NUMBER_OF_ITERATIONS);
    }

    @RepeatedTest(NUMBER_OF_REPEATED_TESTS)
    @DisplayName("Performance Test: QUERIES.forEach(DatabaseManager::executeSqlQuery)")
    void testSqlQueryPerformance() {
        QUERIES.forEach(databaseManager::executeSqlQuery);
    }

    @RepeatedTest(NUMBER_OF_REPEATED_TESTS)
    @DisplayName("Performance Test with Handle: QUERIES.forEach(Handle::execute)")
    void testHandlePerformance() {
        try (final var h = jdbi().open()) {
            QUERIES.forEach(h::execute);
        }
    }

    @RepeatedTest(NUMBER_OF_REPEATED_TESTS)
    @DisplayName("Batch Performance Test: QUERIES.forEach(Batch::add)")
    void testBatchPerformance() {
        try (final var h = jdbi().open()) {
            final var batch = h.createBatch();
            QUERIES.forEach(batch::add);
            batch.execute();
        }
    }

    @RepeatedTest(NUMBER_OF_REPEATED_TESTS)
    @DisplayName("DAO Performance Test: QUERIES.forEach(DAO::insert)")
    void testDaoWithForEachInsertPerformance() {
        QUERIES.forEach(performanceDao::insert);
    }

    @RepeatedTest(NUMBER_OF_REPEATED_TESTS)
    @DisplayName("DAO Batch Performance Test: DAO.insertBatch(QUERIES)")
    void testDaoWithInsertBatchPerformance() {
        performanceDao.insertBatch(QUERIES);
    }

    /**
     * Performance DAO
     */
    public interface PerformanceDao {
        @SqlQuery("SELECT COUNT(*) FROM performance")
        int size();

        @SqlUpdate("INSERT INTO performance (text) VALUES (?)")
        void insert(String text);

        @SqlBatch("INSERT INTO performance (text) VALUES (?)")
        void insertBatch(List<String> texts);
    }
}
