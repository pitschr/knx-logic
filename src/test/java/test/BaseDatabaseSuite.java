package test;

import experimental.api.ComponentFactory;
import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.db.DatabaseManager;
import li.pitschmann.knx.logic.db.H2DatabaseManager;
import li.pitschmann.knx.logic.db.dao.ComponentsDao;
import li.pitschmann.knx.logic.db.dao.ConnectorsDao;
import li.pitschmann.knx.logic.db.dao.EventKeyDao;
import li.pitschmann.knx.logic.db.dao.PinLinksDao;
import li.pitschmann.knx.logic.db.dao.PinValuesDao;
import li.pitschmann.knx.logic.db.dao.PinsDao;
import li.pitschmann.knx.logic.db.loader.InboxComponentLoader;
import li.pitschmann.knx.logic.db.loader.LogicComponentLoader;
import li.pitschmann.knx.logic.db.loader.OutboxComponentLoader;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class BaseDatabaseSuite {
    protected static final DatabaseManager databaseManager = new H2DatabaseManager("localhost", 9093, "junit");
    protected static final LogicComponentLoader logicLoader = new LogicComponentLoader(databaseManager, new ComponentFactory());
    protected static final InboxComponentLoader inboxLoader =  new InboxComponentLoader(databaseManager);
    protected static final OutboxComponentLoader outboxLoader =  new OutboxComponentLoader(databaseManager);

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
     * @param file SQL file to be executed
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

    protected EventKeyDao eventKeyDao() {
        return dao(EventKeyDao.class);
    }

    protected PinValuesDao pinValuesDao()  { return dao(PinValuesDao.class); }

    protected PinLinksDao pinLinksDao() { return dao(PinLinksDao.class); }

    /**
     * Loads all components from the database
     *
     * @return list of {@link Component}
     */
    protected List<Component> loadAllComponents() {
        return componentsDao().all().stream().map(this::loadComponent).collect(Collectors.toList());
    }

    /**
     * Loads a specific component from the database
     *
     * @param id the id of component to be loaded
     * @return the loaded {@link Component}
     */
    @SuppressWarnings("unchecked")
    protected <T extends Component> T loadComponentById(final int id) {
        return (T)loadComponent(componentsDao().find(id));
    }

    private Component loadComponent(final ComponentModel model) {
        switch (model.getComponentType()) {
            case LOGIC:
                return logicLoader.load(model);
            case INBOX:
                return inboxLoader.load(model);
            case OUTBOX:
                return outboxLoader.load(model);
            default:
                throw new AssertionError();
        }
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
        String DROP_AND_CREATE_TABLES = "src/test/resources/dropAndCreateTables.sql";

        interface General {
            String PERFORMANCE_TEST = "src/test/resources/sql/general/performanceTest-createTable.sql";
            String LOAD_SEQUENTIALLY_TEST = "src/test/resources/11-loadSequentiallyTest.sql";
        }

        interface ErrorCases {
            String STATIC_CONNECTOR_MODEL_MULTIPLE_PINS = "src/test/resources/sql/errorCases/staticConnectorModel-multiplePins.sql";
            String DYNAMIC_CONNECTOR_MODEL_STATIC_CONNECTOR = "src/test/resources/sql/errorCases/dynamicConnectorModel-staticConnector.sql";
        }

        interface Logic {
            String A = "src/test/resources/sql/logic/test-logic-A.sql";
            String B = "src/test/resources/sql/logic/test-logic-B.sql";
            String C = "src/test/resources/sql/logic/test-logic-C.sql";
            String D = "src/test/resources/sql/logic/test-logic-D.sql";
            String E = "src/test/resources/sql/logic/test-logic-E.sql";
            String F = "src/test/resources/sql/logic/test-logic-F.sql";
            String G = "src/test/resources/sql/logic/test-logic-G.sql";
            String H = "src/test/resources/sql/logic/test-logic-H.sql";
            String I = "src/test/resources/sql/logic/test-logic-I.sql";
            String J = "src/test/resources/sql/logic/test-logic-J.sql";
            String JAR_FOOBAR = "src/test/resources/sql/logic/jar-logic-foobar.sql";
            String JAR_CORE = "src/test/resources/sql/logic/jar-logic-core.sql";
        }

        interface Inbox {
            String DPT1 = "src/test/resources/sql/inbox/inbox-knx-dpt1.sql";
            String DPT2 = "src/test/resources/sql/inbox/inbox-knx-dpt2.sql";
            String VAR = "src/test/resources/sql/inbox/inbox-knx-var.sql";
        }

        interface Outbox {
            String DPT1 = "src/test/resources/sql/outbox/outbox-knx-dpt1.sql";
            String DPT2 = "src/test/resources/sql/outbox/outbox-knx-dpt2.sql";
            String VAR = "src/test/resources/sql/outbox/outbox-knx-var.sql";
        }

        interface PinValues {
            String NO_PIN_VALUES = "src/test/resources/sql/pinValues/noPinValues.sql";
            String LAST_VALUE_SINGLE = "src/test/resources/sql/pinValues/lastValue-single.sql";
            String LAST_VALUE_MULTIPLE = "src/test/resources/sql/pinValues/lastValue-multiple.sql";
            String LAST_VALUE_TYPES = "src/test/resources/sql/pinValues/lastValue-types.sql";
        }

        interface Diagram {
            String EMPTY = "src/test/resources/sql/diagram/empty.sql";
            String SIMPLE_NEGATION = "src/test/resources/sql/diagram/simple-negationLogic.sql";
            String SIMPLE_AND = "src/test/resources/sql/diagram/simple-andLogic.sql";
        }
    }
}
