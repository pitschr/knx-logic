package li.pitschmann.knx.logic.db;

import org.jdbi.v3.core.Jdbi;

import java.io.File;
import java.nio.file.Path;

/**
 * Interface for Database Manager
 */
public interface DatabaseManager {

    /**
     * Returns the {@link Jdbi} for current {@link DatabaseManager}
     *
     * @return the {@link Jdbi} instance
     */
    Jdbi jdbi();

    /**
     * Returns the DAO component from jdbi
     *
     * @param daoClass the class of DAO component
     * @return instance of DAO
     */
    <T> T dao(Class<T> daoClass);

    /**
     * Starts up the database
     */
    void startServer();

    /**
     * Stops the database
     */
    void stopServer();

    /**
     * Creates a backup of the current database
     */
    Path createBackup();

    /**
     * Executes the given SQL {@code file}
     *
     * @param file the SQL file that contains SQL queries
     */
    void executeSqlFile(final File file);

    /**
     * Executes the given SQL {@code query}
     *
     * @param query the SQL query to be executed
     */
    void executeSqlQuery(final String query);

    /**
     * Persists the given {@code object} to the database
     *
     * @param object the object to be persisted
     * @return the returned primary key
     */
    int save(final Object object);

    /**
     * Returns if the database is currently running
     *
     * @return {@code true} if the database server is running, otherwise {@code false}
     */
    boolean isRunning();

    /**
     * Returns the host name of the database server
     *
     * @return host name
     */
    String getHostname();

    /**
     * Returns the used port by the database server
     *
     * @return port
     */
    int getPort();

    /**
     * Returns the actively used schema name of database
     *
     * @return schema name
     */
    String getSchemaName();
}
