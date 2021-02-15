package li.pitschmann.knx.logic.components;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.ConnectorFactory;
import li.pitschmann.knx.logic.pin.Pin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Life-Cycle of Logic Component
 *
 * <pre>
 *                             .-- (yes) -- [init] --.                            .--- (yes) -- [logic] ---.
 *                            /                       \                          /                          \
 * =>>> [start] -- (init?) --<                         >-- (init or trigger?) --<                            >-- [end] =>>>
 *                            \                       /                          \                          /
 *                             `-- (no) -------------´                            `--- (no) ---------------´
 *
 * </pre>
 *
 * @author PITSCHR
 */
public final class LogicComponentImpl extends AbstractExecutableComponent<Logic> implements LogicComponent {
    private static final Logger LOG = LoggerFactory.getLogger(LogicComponentImpl.class);
    private final List<Connector> inputConnectors;
    private final List<Connector> outputConnectors;
    private long logicCount;
    private int numberOfPreviousInputPins = -1;

    /**
     * Creates a new wrapper instance of {@link LogicComponent} for given {@link Logic} instance
     *
     * @param logic an instance of logic that should be wrapped to a component
     */
    public LogicComponentImpl(final Logic logic) {
        super(logic);
        inputConnectors = ConnectorFactory.getInputConnectors(logic);
        outputConnectors = ConnectorFactory.getOutputConnectors(logic);
    }

    /***
     * Internal execution body. This is thread-safe.
     */
    @Override
    protected void executeSafe() {
        // starts logic
        getWrappedObject().start();

        final var inputs = this.getInputPins();
        final var outputs = this.getOutputPins();

        // check if there is at least one input marked as refreshed
        boolean anyInputRefreshed = false;
        if (numberOfPreviousInputPins != inputs.size()) {
            // the current number of inputs differs with the number of inputs we had previously
            // e.g. when an input pin has been removed
            numberOfPreviousInputPins = inputs.size();
            anyInputRefreshed = true;
        } else {
            for (final var input : inputs) {
                if (input.isRefresh() || input.isAlwaysTrigger()) {
                    anyInputRefreshed = true;
                    break;
                }
            }
        }
        inputs.forEach(Pin::clearRefresh);

        // initialize?
        final var init = this.executedCount() == 0;
        if (init) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Enter init body for '{}'", this.getUid());
            }
            // initialization
            getWrappedObject().init();

            // execute logic and increment the logic counter
            getWrappedObject().logic();
            logicCount++;

            // mark all outputs as 'refreshed' on initialization,
            // components listening on outputs should be notified
            // even if there was no value change!
            outputs.forEach(Pin::setRefresh);
        } else {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Enter normal body for '{}'", this.getUid());
            }

            // not initialization - clear 'refresh' flag for all
            // outputs we will figure out which values have been changed
            outputs.forEach(Pin::clearRefresh);

            // execute logic only when at least one input was marked as 'refreshed'
            // and then figure out which output pins have been changed
            if (anyInputRefreshed) {
                // store outputs for later comparison
                final var oldOutputValues = outputs.stream().map(Pin::getValue).toArray();

                // execute logic and increment the logic counter
                getWrappedObject().logic();
                logicCount++;

                // set refresh flag for changed output values
                for (var i = 0; i < outputs.size(); i++) {
                    final var output = outputs.get(i);
                    final var valueChanged = !Objects.equals(output.getValue(), oldOutputValues[i]);
                    if (valueChanged || output.isAlwaysTrigger()) {
                        output.setRefresh();
                    }
                }
            }
        }

        // end logic
        getWrappedObject().end();
    }

    @Override
    public final long logicCount() {
        return this.logicCount;
    }

    @Override
    public List<Connector> getConnectors() {
        final var list = new ArrayList<Connector>(inputConnectors.size() + outputConnectors.size());
        list.addAll(inputConnectors);
        list.addAll(outputConnectors);
        return List.copyOf(list);
    }

    @Override
    public final List<Connector> getInputConnectors() {
        return inputConnectors;
    }

    @Override
    public final List<Connector> getOutputConnectors() {
        return outputConnectors;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("uid", getUid()) //
                .add("logicClass", getWrappedObject().getClass().getName()) //
                .add("executedCount", executedCount()) //
                .add("executedTime", executedTime()) //
                .add("logicCount", logicCount) //
                .add("inputConnectors", inputConnectors.size()) //
                .add("outputConnectors", outputConnectors.size()) //
                .toString();
    }
}
