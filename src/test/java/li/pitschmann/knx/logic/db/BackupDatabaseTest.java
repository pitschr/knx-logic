package li.pitschmann.knx.logic.db;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import test.BaseDatabaseSuite;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
 * Database Backup Test
 *
 * @author PITSCHR
 */
final class BackupDatabaseTest extends BaseDatabaseSuite {
    private static final Path BACKUP_PATH = Paths.get("backup");

    @BeforeAll
    static void cleanUpBackupFolder() {
        // clean up backup folder (otherwise it would become bigger and bigger)
        try {
            if (Files.exists(BACKUP_PATH)) {
                Files.walk(BACKUP_PATH).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                assertThat(Files.exists(BACKUP_PATH)).isFalse(); // folder should be gone now!
            }
        } catch (final IOException e) {
            fail("Could not delete the backup folder!", e);
        }
    }

    /**
     * Backup Test for {@link DatabaseManager#createBackup()}
     */
    @Test
    void createBackupH2() {
        final Path backupPath = databaseManager.createBackup();
        assertThat(Files.exists(backupPath)).isTrue();
    }

}
