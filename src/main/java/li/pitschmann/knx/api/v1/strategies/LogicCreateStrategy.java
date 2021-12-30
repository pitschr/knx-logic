package li.pitschmann.knx.api.v1.strategies;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.LogicRepository;
import li.pitschmann.knx.logic.components.LogicComponent;
import li.pitschmann.knx.logic.components.LogicComponentImpl;
import li.pitschmann.knx.logic.exceptions.LoaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

/**
 * {@link CreateStrategy} implementation for {@link LogicComponent}
 *
 * @author PITSCHR
 */
public class LogicCreateStrategy implements CreateStrategy<LogicComponent> {
    private static final Logger LOG = LoggerFactory.getLogger(LogicCreateStrategy.class);
    private final LogicRepository logicRepository;

    public LogicCreateStrategy(final LogicRepository logicRepository) {
        this.logicRepository = Objects.requireNonNull(logicRepository);
    }

    /**
     * Creates a new instance of {@link Logic} by given {@code logicClass}
     *
     * @param logicClass to be loaded; may not be null
     * @return a new instance of {@link Logic}
     */
    private static Logic createLogicInstance(final Class<? extends Logic> logicClass) {
        final Logic logic;
        try {
            logic = logicClass.getConstructor().newInstance();
            LOG.debug("New instance of logic created successfully: {}", logic);
        } catch (final ReflectiveOperationException e) {
            throw new LoaderException("Could not load logic class: " + logicClass, e);
        }
        return logic;
    }

    @Override
    public LogicComponent apply(final Map<String, String> data) {
        // extracts Logic class from data
        final var logicClassAsString = Objects.requireNonNull(data.get("class"));

        // try to find the logic class and load it
        final var logicClass = logicRepository.findLogicClass(logicClassAsString);
        LOG.debug("Logic Class: {}", logicClass);

        return new LogicComponentImpl(createLogicInstance(logicClass));
    }
}
