package test;

import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.H2DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.db.dao.ObjectsDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseDatabaseSuite {
    protected static final DatabaseManager databaseManager = new H2DatabaseManager("localhost", 9093, "junit");

    /**
     * Returns the DAO for JUnit Database Manager
     *
     * @param daoClass the expected class of DAO class to be returned
     * @return the instance of DAO class
     */
    protected <T> T dao(final Class<T> daoClass) {
        return databaseManager.dao(daoClass);
    }

    /**
     * Returns the JDBI for JUnit Database Manager
     *
     * @return the JDBI from
     */
    protected Jdbi jdbi() {
        return databaseManager.jdbi();
    }

    /**
     * Executes the given SQL {@code file}
     *
     * @param file
     */
    protected void executeSqlFile(final File file) {
        databaseManager.executeSqlFile(file);
    }

    /**
     * Executes the given SQL {@code query}
     *
     * @param query
     */
    protected void executeSqlQuery(final String query) {
        databaseManager.executeSqlQuery(query);
    }

    protected int save(final Object object) {
        return databaseManager.save(object);
    }

    protected ComponentsDao componentsDao() {
        return dao(ComponentsDao.class);
    }

    protected ConnectorsDao connectorsDao() {
        return dao(ConnectorsDao.class);
    }

    protected PinsDao pinsDao() {
        return dao(PinsDao.class);
    }

    protected ObjectsDao objectsDao() {
        return dao(ObjectsDao.class);
    }

    protected EventKeyDao eventKeyDao() {
        return dao(EventKeyDao.class);
    }

    /**
     * Start up database
     */
    @BeforeEach
    public void startDatabase() {
        assertThat(databaseManager.isRunning()).isFalse();
        beforeDatabaseStart(databaseManager);
        databaseManager.startServer();
        assertThat(databaseManager.isRunning()).isTrue();

        databaseManager.executeSqlFile(new File(BaseDatabaseSuite.Sql.DROP_AND_CREATE_TABLES));
        assertThat(componentsDao().size()).isZero();
        assertThat(connectorsDao().size()).isZero();
        assertThat(pinsDao().size()).isZero();

        afterDatabaseStart(databaseManager);
    }

    /**
     * Actions to be done BEFORE database startup
     *
     * @param databaseManager the database manager
     */
    public void beforeDatabaseStart(final DatabaseManager databaseManager) {
        // NO-OP
    }

    /**
     * Actions to be done AFTER database startup
     *
     * @param databaseManager the database manager
     */
    public void afterDatabaseStart(final DatabaseManager databaseManager) {
        // NO-OP
    }

    /**
     * Stop database
     */
    @AfterEach
    public void stopDatabase() {
        assertThat(databaseManager.isRunning()).isTrue();
        beforeDatabaseStop(databaseManager);
        databaseManager.stopServer();
        assertThat(databaseManager.isRunning()).isFalse();
        afterDatabaseStop(databaseManager);
    }

    /**
     * Actions to be don BEFORE database stop
     *
     * @param databaseManager the database manager
     */
    public void beforeDatabaseStop(final DatabaseManager databaseManager) {
        // NO-OP
    }

    /**
     * Actions to be done AFTER database stop
     *
     * @param databaseManager the database manager
     */
    public void afterDatabaseStop(final DatabaseManager databaseManager) {
        // NO-OP
    }


    /**
     * Database SQL files
     */
    public interface Sql {
        String DROP_AND_CREATE_TABLES = "src/test/resources/00-dropAndCreateTables.sql";
        String INSERT_LOGIC_COMPONENT_SAMPLES = "src/test/resources/01-importLogicComponents.sql";
        String INSERT_INBOX_COMPONENT_SAMPLES = "src/test/resources/02-importInboxComponents.sql";
        String INSERT_OUTBOX_COMPONENT_SAMPLES = "src/test/resources/03-importOutboxComponents.sql";
        String PERFORMANCE_TEST = "src/test/resources/10-performanceTest.sql";
        String LOAD_SEQUENTIALLY_TEST = "src/test/resources/11-loadSequentiallyTest.sql";
        String STATIC_CONNECTOR_DYNAMIC_PINS = "src/test/resources/12-testStaticConnectorModelWithMultiplePins.sql";
        String STATIC_CONNECTOR_DYNAMIC_CONNECTORMODEL = "src/test/resources/13-testStaticConnectorDynamicConnectorModel.sql";
    }
}
