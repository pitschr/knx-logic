package test.components;

import li.pitschmann.knx.logic.Logic;
import li.pitschmann.knx.logic.annotations.Input;
import li.pitschmann.knx.logic.annotations.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test component with static inputs only. All inputs and outputs are primitives.
 */
public final class AndLogicBooleanPrimitive implements Logic {
    protected static final Logger LOG = LoggerFactory.getLogger(AndLogicBooleanPrimitive.class);

    @Input
    private boolean i1, i2 = true;

    @Output
    private boolean o;

    @Override
    public void logic() {
        this.debug("LOGIC ");
        this.o = this.i1 && this.i2;
    }

    /* DEBUG PURPOSES ONLY - START */
    @Override
    public void start() {
        this.debug("START ");
    }

    @Override
    public void init() {
        this.debug("INIT  ");
    }

    @Override
    public void end() {
        this.debug("END   ");
    }

    private void debug(String stepName) {
        LOG.debug("Step({}): i1={}, i2={}, o={}", stepName, this.i1, this.i2, this.o);
    }
    /* DEBUG PURPOSES ONLY - END */
}
