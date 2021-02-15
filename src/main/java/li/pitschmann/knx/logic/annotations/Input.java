package li.pitschmann.knx.logic.annotations;

import li.pitschmann.knx.logic.Trigger;
import li.pitschmann.knx.logic.pin.DynamicPin;
import li.pitschmann.knx.logic.pin.Pin;
import li.pitschmann.knx.logic.pin.StaticPin;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Annotation for 'Input' fields for declaration that the field
 * is designed to receive values from other component via the connector
 * holding one or several {@link Pin}.</p>
 *
 * <pre>{@code
 * @Input
 * private boolean boolValue;
 *
 * @Input
 * private String[] strValues;
 * }</pre>
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Input {

    /**
     * <p>Minimum of input values that can be hold by {@link Pin}.</p>
     *
     * <p>This setting is effectively for {@link DynamicPin} only as the number of
     * pin can be re-sized. The value may not be negative.</p>
     *
     * <p>Default minimum size is {@code 1} which means there should be at least
     * one {@link Pin} present.</p>
     *
     * <p>This setting will be ignored for {@link StaticPin}, for {@link StaticPin}
     * is is always {@code 1}</p>
     *
     * @return number of minimum values that can be hold; negative value not allowed
     */
    int min() default 1;

    /**
     * <p>Maximum of input values that can be hold by {@link DynamicPin}.</p>
     *
     * <p>This setting is effectively for {@link DynamicPin} only as the number of
     * pins can be re-sized. The value may not be negative and must equal or greater
     * than {@link #min()} value.</p>
     *
     * <p>Default maximum size is {@link Integer#MAX_VALUE} which can be considered
     * as unbound/unlimited.</p>
     *
     * <p>This setting will be ignored for {@link StaticPin}, for {@link StaticPin}
     * is is always {@code 1}</p>
     *
     * @return number of maximum values that can be hold; must equal or larger than
     * {@link #min()} value
     */
    int max() default Integer.MAX_VALUE;

    /**
     * <p>Defines the behavior of trigger when the {@link Pin} should be triggered.</p>
     *
     * <p>Default is {@link Trigger#ON_INIT_AND_CHANGE}. See {@link Trigger}.</p>
     *
     * @return the trigger setting
     */
    Trigger trigger() default Trigger.ON_INIT_AND_CHANGE;
}
