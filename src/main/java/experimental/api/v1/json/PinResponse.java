package experimental.api.v1.json;

import li.pitschmann.knx.logic.pin.Pin;

public final class PinResponse {
    private final String uid;
    private final String connectorUid;
    private final String value;

    private PinResponse(final String uid, final String connectorUid, final String value) {
        this.uid = uid;
        this.connectorUid = connectorUid;
        this.value = value;
    }

    public static PinResponse fromWithoutConnectorUid(final Pin pin) {
        return new PinResponse(
                pin.getUid().toString(),
                null,
                String.valueOf(pin.getValue())
        );
    }

    public static PinResponse from(final Pin pin) {
        return new PinResponse(
                pin.getUid().toString(),
                pin.getConnector().getUid().toString(),
                String.valueOf(pin.getValue())
        );
    }

    public String getUid() {
        return uid;
    }

    public String getConnectorUid() {
        return connectorUid;
    }

    public String getValue() {
        return value;
    }
}
