package experimental.api.v1.json;

import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.DynamicConnector;

import java.util.List;
import java.util.stream.Collectors;

public final class ConnectorResponse {
    private final String uid;
    private final String name;
    private final boolean dynamic;
    private final String pinType;
    private final List<PinResponse> pins;

    private ConnectorResponse(final String uid, final String name, boolean dynamic, final String pinType, final List<PinResponse> pins) {
        this.uid = uid;
        this.name = name;
        this.dynamic = dynamic;
        this.pinType = pinType;
        this.pins = pins;
    }

    public static ConnectorResponse from(final Connector connector) {
        return new ConnectorResponse(
                connector.getUid().toString(),
                connector.getDescriptor().getName(),
                connector instanceof DynamicConnector,
                connector.getDescriptor().getFieldType().getSimpleName().toLowerCase(),
                connector.getPinStream().map(PinResponse::fromWithoutConnectorUid).collect(Collectors.toUnmodifiableList())
        );
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getPinType() {
        return pinType;
    }

    public List<PinResponse> getPins() {
        return pins;
    }

    public boolean isDynamic() {
        return dynamic;
    }
}
