package li.pitschmann.knx.logic.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Abstract Executable Component containing wrapped object and considered
 * to be executable with thread-safe in mind.
 *
 * @param <T> type of object to be wrapped by the component
 * @author PITSCHR
 */
abstract class AbstractExecutableComponent<T> extends AbstractComponent<T> implements ExecutableComponent {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractExecutableComponent.class);
    private final Lock executeLock = new ReentrantLock();
    private long executedCount;
    private long executedTime;

    /**
     * Package-protected constructor that wraps the object
     *
     * @param object to be wrapped; may not be null
     */
    AbstractExecutableComponent(final T object) {
        super(object);
    }

    /**
     * Internal execution body. Statements inside this method is thread-safe.
     */
    protected abstract void executeSafe();

    @Override
    public void execute() {
        LOG.trace("START 'execute()' method for '{}'", getUid());

        executeLock.lock();
        final var start = System.nanoTime();
        try {
            executeSafe();
        } finally {
            // update statistics
            executedCount++;
            executedTime += System.nanoTime() - start;

            executeLock.unlock();
            LOG.trace("END 'execute()' method for '{}'", getUid());
        }
    }

    @Override
    public final long executedCount() {
        return executedCount;
    }

    @Override
    public long executedTime() {
        return executedTime;
    }
}
