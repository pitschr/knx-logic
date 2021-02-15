package li.pitschmann.knx.logic;

import li.pitschmann.knx.logic.pin.Pin;

/**
 * Types of trigger when a {@link Pin} should be considered as triggered.
 * Using this trigger types we are able to fine-control the behavior of
 * logic components.
 *
 * <pre>{@code
 * @Input(trigger = Trigger.ALWAYS)
 * private boolean boolValue;
 *
 * @Output(trigger = Trigger.ON_INIT_AND_CHANGE)
 * private String[] strValues;
 * }</pre>
 *
 * @author PITSCHR
 */
public enum Trigger {

    /**
     * <p>Triggers always, regardless if there was change made or not.
     * Also triggered on initialization.</p>
     */
    ALWAYS,

    /**
     * <p>Triggers on initialization and if there was a value change.</p>
     */
    ON_INIT_AND_CHANGE
}
