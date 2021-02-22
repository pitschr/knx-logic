package li.pitschmann.knx.logic;

import li.pitschmann.knx.core.utils.Maps;
import li.pitschmann.knx.core.utils.Stopwatch;
import li.pitschmann.knx.logic.exceptions.NoLogicClassFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * The repository of all available classes that implements
 * the {@link Logic} interface.
 *
 * @author PITSCHR
 */
public final class LogicRepository {
    private static final Logger LOG = LoggerFactory.getLogger(LogicRepository.class);
    private final Map<String, Class<Logic>> logicMap = Maps.newHashMap(100);

    /**
     * Registers all {@link Logic} implementations that is referenced to the path.
     *
     * @param path the path of JAR file, may be a folder to scan the entire folder for JAR files
     * @throws IOException in case there an I/O exception occurred
     */
    public void scanLogicClasses(final Path path) throws IOException {
        final var sw = Stopwatch.createStarted();
        final var allLogicClasses = ComponentScanner.scanByFileOrFolder(path, Logic.class);
        final var elapsedTime = sw.elapsed(TimeUnit.MILLISECONDS);

        allLogicClasses.forEach(logicClass -> logicMap.put(logicClass.getName(), logicClass));

        if (LOG.isDebugEnabled()) {
            LOG.debug("{} logic classes found ({} ms):{}{}",
                    allLogicClasses.size(),
                    elapsedTime,
                    System.lineSeparator(),
                    allLogicClasses
                            .stream()
                            .map(logicClass -> " * " + logicClass.getName() + " (Class Loader: " + logicClass.getClassLoader() + ")")
                            .collect(Collectors.joining(System.lineSeparator()))
            );
        }
    }

    /**
     * Returns an immutable list of registered {@link Logic} classes
     *
     * @return immutable list of {@link Logic} classes
     */
    public List<Class<Logic>> getAllLogicClasses() {
        return List.copyOf(logicMap.values());
    }

    /**
     * Returns the {@link Logic} class that contains {@link ClassLoader} information
     * to create a new instance of {@link Logic}.
     *
     * @param classAsString class as string representation for look-up
     * @return the {@link Logic} class
     * @throws NoLogicClassFound if no suitable {@link Logic} class was found
     */
    public Class<Logic> findLogicClass(final String classAsString) {
        final var logicClass = logicMap.get(classAsString);
        if (logicClass != null) {
            LOG.debug("Logic class '{}' found: {}", classAsString, logicClass);
            return logicClass;
        }

        // print out all logic classes
        if (LOG.isDebugEnabled()) {
            LOG.debug("No logic class '{}' found in:\n{}", classAsString, logicMap);
        }

        throw new NoLogicClassFound("No Logic Class found: " + classAsString);
    }
}
