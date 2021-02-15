package li.pitschmann.knx.logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Scans JAR or ZIP file for all components
 *
 * @author PITSCHR
 */
public final class ComponentScanner {
    private static final Logger LOG = LoggerFactory.getLogger(ComponentScanner.class);
    /**
     * Maximum directory levels to be scanned in a folder.
     */
    private static final int FOLDER_MAX_DEPTH = 5;
    /**
     * Matcher for all file names that are subject to be scanned.
     */
    private static final BiPredicate<Path, BasicFileAttributes> JAR_FILE_MATCHER =
            (filePath, fileAttr) -> filePath.toString().toLowerCase().endsWith(".jar");

    private ComponentScanner() {
        throw new AssertionError("Don't touch me!");
    }

    /**
     * <p>Scans for classes in given path which may be a folder or a file.</p>
     * <p>Only files with {@code *.jar} extension are considered to be scanned.</p>
     *
     * @param path  the path of file or folder to be scanned; may not be null
     * @param clazz the class to be filtered for; may not be null
     * @param <T>   type of classes to be returned
     * @return immutable list of elements that extends or implements {@code <T>}
     * @throws IOException in case there was an I/O exception
     */
    public static <T> List<Class<T>> scanByFileOrFolder(final Path path, Class<T> clazz) throws IOException {
        Objects.requireNonNull(path);
        Objects.requireNonNull(clazz);

        final List<Class<T>> list;
        if (Files.isRegularFile(path)) {
            list = scanByFile(path, clazz);
        } else {
            list = scanByFolder(path, clazz);
        }
        return List.copyOf(list);
    }

    /**
     * Scans for classes in a regular ZIP file
     *
     * @param path  the path of regular file to be scanned; may not be null
     * @param clazz the class to be filtered for; may not be null
     * @param <T>   type of classes to be returned
     * @return new mutable list of elements that extends or implements {@code <T>}
     * @throws IOException in case there was an I/O exception
     */
    private static <T> List<Class<T>> scanByFile(final Path path, Class<T> clazz) throws IOException {
        final var file = path.toFile();
        try (final var inputStream = new ZipInputStream(new FileInputStream(file))) {
            return scanByClass(
                    URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, ComponentScanner.class.getClassLoader()), //
                    inputStream, //
                    clazz //
            );
        }
    }

    /**
     * Scans for classes in a folder
     *
     * @param path  the path of folder to be scanned; may not be null
     * @param clazz the class to be filtered for; may not be null
     * @param <T>   type of classes to be returned
     * @return new mutable list of elements that extends or implements {@code <T>}
     * @throws IOException in case there was an I/O exception
     */
    private static <T> List<Class<T>> scanByFolder(final Path path, Class<T> clazz) throws IOException {
        try (final var s = Files.find(path, FOLDER_MAX_DEPTH, JAR_FILE_MATCHER)) {
            return s.flatMap(f -> {
                try {
                    LOG.debug("Scan file: {}", f);
                    return scanByFile(f, clazz).stream();
                } catch (final IOException e) {
                    LOG.warn("Could not scan file and will be skipped: {}", f, e);
                    return Stream.empty();
                }
            }).collect(Collectors.toList());
        }
    }

    /**
     * Scans for classes in a {@link ZipInputStream} and loads into the {@link ClassLoader}
     *
     * @param classLoader the class loader to be used by the application
     * @param inputStream the ZIP input stream to be scanned
     * @param clazz       the class to be filtered for; may not be null
     * @param <T>         type of classes to be returned
     * @return new mutable list of elements that extends or implements {@code <T>}
     * @throws IOException in case there was an I/O exception
     */
    @SuppressWarnings("unchecked")
    private static <T> List<Class<T>> scanByClass(final ClassLoader classLoader, final ZipInputStream inputStream, final Class<T> clazz) throws IOException {
        final var classes = new LinkedList<Class<T>>();
        ZipEntry entry;
        while ((entry = inputStream.getNextEntry()) != null) {
            if (entry.getName().endsWith(".class")) {
                final var fileName = entry.getName().replace("/", ".");
                final var className = fileName.substring(0, fileName.length() - 6); // remove '.class' pattern
                LOG.trace("Trying to load class: {}", className);

                // does this class implements the desired interface or extends the class?
                try {
                    final var tmpClass = Class.forName(className, false, classLoader);
                    if (!tmpClass.isInterface() && clazz.isAssignableFrom(tmpClass)) {
                        LOG.debug("Class implements '{}': {}", clazz, tmpClass);
                        classes.add((Class<T>) tmpClass);
                    }
                } catch (final ClassNotFoundException | NoClassDefFoundError e) {
                    // do nothing...
                }
            }
        }
        return classes;
    }
}
