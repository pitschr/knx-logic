package experimental.api.v1.json;

import li.pitschmann.knx.core.utils.Strings;

/**
 * JSON Payload Request to set a value for a pin
 * <p>
 * <pre>{@code
 *   {"value":"true"}
 * }</pre>
 */
public final class PinSetValueRequest {
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("value", value)
                .toString();
    }
}
