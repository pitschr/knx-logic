package li.pitschmann.knx.api.v1.json;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.pin.Pin;

/**
 * API Response for a single {@link Pin}
 * <p>
 * Example Response:
 * <pre>{@code
 *      {
 *          "uid": "uid-pin-logic-input-1",
 *          "connectorUid": "uid-connector-logic-input",
 *          "value": "false"
 *      }
 * }</pre>
 */
public final class PinResponse {
    private final String uid;
    private final String connectorUid;
    private final String valueType;
    private final String value;

    private PinResponse(final String uid, final String connectorUid, final String valueType, final String value) {
        this.uid = uid;
        this.connectorUid = connectorUid;
        this.valueType = valueType;
        this.value = value;
    }

    public static PinResponse fromWithoutConnectorInfo(final Pin pin) {
        return new PinResponse(
                pin.getUid().toString(),
                null,
                null,
                String.valueOf(pin.getValue())
        );
    }

    public static PinResponse fromWithoutPinInfo(final Pin pin) {
        return new PinResponse(
                null,
                null,
                pin.getDescriptor().getFieldType().getName(),
                String.valueOf(pin.getValue())
        );
    }

    public static PinResponse from(final Pin pin) {
        return new PinResponse(
                pin.getUid().toString(),
                pin.getConnector().getUid().toString(),
                pin.getDescriptor().getFieldType().getName(),
                String.valueOf(pin.getValue())
        );
    }

    public String getUid() {
        return uid;
    }

    public String getConnectorUid() {
        return connectorUid;
    }

    public String getValueType() {
        return valueType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("uid", uid)
                .add("connectorUid", connectorUid)
                .add("valueType", valueType)
                .add("value", value)
                .toString();
    }
}
