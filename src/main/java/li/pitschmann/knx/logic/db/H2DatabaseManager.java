package li.pitschmann.knx.logic.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import li.pitschmann.knx.logic.db.jdbi.mappers.argument.BindingTypeArgumentFactory;
import li.pitschmann.knx.logic.db.jdbi.mappers.argument.ClassArgumentFactory;
import li.pitschmann.knx.logic.db.jdbi.mappers.argument.ComponentTypeArgumentFactory;
import li.pitschmann.knx.logic.db.jdbi.mappers.argument.EventKeyArgumentFactory;
import li.pitschmann.knx.logic.db.jdbi.mappers.argument.UIDArgumentFactory;
import li.pitschmann.knx.logic.db.jdbi.mappers.column.BindingTypeColumnMapper;
import li.pitschmann.knx.logic.db.jdbi.mappers.column.ClassColumnMapper;
import li.pitschmann.knx.logic.db.jdbi.mappers.column.ComponentTypeColumnMapper;
import li.pitschmann.knx.logic.db.jdbi.mappers.column.EventKeyColumnMapper;
import li.pitschmann.knx.logic.db.jdbi.mappers.column.UIDColumnMapper;
import li.pitschmann.knx.logic.db.models.ComponentModel;
import li.pitschmann.knx.logic.db.models.ConnectorModel;
import li.pitschmann.knx.logic.db.models.DiagramComponentModel;
import li.pitschmann.knx.logic.db.models.DiagramLinkModel;
import li.pitschmann.knx.logic.db.models.DiagramModel;
import li.pitschmann.knx.logic.db.models.EventKeyModel;
import li.pitschmann.knx.logic.db.models.PinLinkModel;
import li.pitschmann.knx.logic.db.models.PinModel;
import li.pitschmann.knx.logic.db.persistence.ConnectorPersistenceStrategy;
import li.pitschmann.knx.logic.db.persistence.InboxComponentPersistenceStrategy;
import li.pitschmann.knx.logic.db.persistence.LogicComponentPersistenceStrategy;
import li.pitschmann.knx.logic.db.persistence.OutboxComponentPersistenceStrategy;
import org.h2.tools.Server;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.reflect.FieldMapper;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * {@link DatabaseManager} implementation for H2 database
 *
 * @author PITSCHR
 */
public final class H2DatabaseManager implements DatabaseManager {
    private static final Logger LOG = LoggerFactory.getLogger(H2DatabaseManager.class);
    private static final String[] DB_SETTINGS = new String[]{"AUTO_SERVER=TRUE", "AUTO_RECONNECT=TRUE"};
    private final AtomicBoolean running = new AtomicBoolean();
    private final String hostname;
    private final String schema;
    private final int port;
    private final JdbiDaoPool jdbiDaoPool;
    private final PersistenceManager persistenceManager;
    private Jdbi jdbi;
    private Server dbServer;

    /**
     * Creates a new Database Manager
     *
     * @param hostname the host name
     * @param port     the port
     * @param schema   the schema
     */
    public H2DatabaseManager(final String hostname, final int port, final String schema) {
        this.hostname = Objects.requireNonNull(hostname);
        this.schema = Objects.requireNonNull(schema);
        this.port = port;
        this.jdbiDaoPool = new JdbiDaoPool(this);

        // register persistence strategies
        persistenceManager = new PersistenceManager();
        persistenceManager.addPersistenceStrategy(new LogicComponentPersistenceStrategy(this));
        persistenceManager.addPersistenceStrategy(new InboxComponentPersistenceStrategy(this));
        persistenceManager.addPersistenceStrategy(new OutboxComponentPersistenceStrategy(this));
        persistenceManager.addPersistenceStrategy(new ConnectorPersistenceStrategy(this));
    }

    @Override
    public Jdbi jdbi() {
        return jdbi;
    }

    @Override
    public <T> T dao(Class<T> daoClass) {
        return jdbiDaoPool.getDaoOnDemand(daoClass);
    }

    @Override
    public int save(final Object object) {
        return persistenceManager.save(object);
    }

    @Override
    public void startServer() {
        if (!this.running.getAndSet(true)) {
            // start H2 database
            try {
                final var startArguments = new String[]{"-tcp", "-tcpAllowOthers", "-tcpPort", String.valueOf(this.port)};
                if (LOG.isDebugEnabled()) {
                    LOG.debug("DB Startup arguments: {}", Arrays.toString(startArguments));
                }
                dbServer = Server.createTcpServer(startArguments).start();
            } catch (final Exception e) {
                if (e.getCause() instanceof BindException) {
                    LOG.info("DB maybe already in use? Try to continue with establishing connection.");
                } else {
                    LOG.error("Could not start database!", e);
                    throw new IllegalStateException(e);
                }
            }

            // connect to H2 database
            final var jdbcUrl = String.format("jdbc:h2:file:~/%s;%s", schema, String.join(";", DB_SETTINGS));
            LOG.debug("JDBC URL: {}", jdbcUrl);

            // Connection pooling for faster performance
            final var config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername("");
            config.setPassword("");

            // Create JDBI
            jdbi = Jdbi.create(new HikariDataSource(config));
            jdbi.installPlugin(new SqlObjectPlugin());

            // register Jdbi Mappers
            jdbi.registerRowMapper(FieldMapper.factory(ComponentModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(ConnectorModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(PinModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(PinLinkModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(EventKeyModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(DiagramModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(DiagramComponentModel.class));
            jdbi.registerRowMapper(FieldMapper.factory(DiagramLinkModel.class));

            jdbi.registerArgument(new UIDArgumentFactory());
            jdbi.registerArgument(new BindingTypeArgumentFactory());
            jdbi.registerArgument(new ClassArgumentFactory());
            jdbi.registerArgument(new ComponentTypeArgumentFactory());
            jdbi.registerArgument(new EventKeyArgumentFactory());

            jdbi.registerColumnMapper(new UIDColumnMapper());
            jdbi.registerColumnMapper(new BindingTypeColumnMapper());
            jdbi.registerColumnMapper(new ClassColumnMapper());
            jdbi.registerColumnMapper(new ComponentTypeColumnMapper());
            jdbi.registerColumnMapper(new EventKeyColumnMapper());

            LOG.info("Database is running NOW!");
        } else {
            throw new AssertionError("Database is already running!");
        }
    }

    @Override
    public void stopServer() {
        if (this.running.getAndSet(false)) {
            try {
                dbServer.stop();
                LOG.info("Database is stopped NOW!");
            } catch (final Exception e) {
                LOG.error("Something went wrong during stopping the database!", e);
            } finally {
                dbServer = null;
                jdbi = null;
                jdbiDaoPool.clear();
            }
        } else {
            throw new AssertionError("Database is already stopped!");
        }
    }

    @Override
    public Path createBackup() {
        final var backupPath = Paths.get("backup/h2_" + schema + "_backup" + System.currentTimeMillis() + ".zip");
        jdbi.useHandle(h -> h.execute("BACKUP TO '" + backupPath + "'"));
        return backupPath;
    }

    @Override
    public void executeSqlFile(final File file) {
        try {
            jdbi.useHandle(h -> h.createScript(Files.readString(file.toPath(), StandardCharsets.UTF_8)).execute());
        } catch (IOException e) {
            LOG.error("Could not execute the SQL file '{}'", file, e);
        }
    }

    @Override
    public void executeSqlQuery(final String query) {
        jdbi.useHandle(h -> h.execute(query));
    }

    @Override
    public boolean isRunning() {
        return this.running.get();
    }

    @Override
    public String getHostname() {
        return this.hostname;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }
}
