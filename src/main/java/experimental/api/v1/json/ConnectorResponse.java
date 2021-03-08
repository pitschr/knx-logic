package experimental.api.v1.json;

import java.util.List;

public class ConnectorResponse {
    private String name;
    private boolean dynamic;
    private String pinType;
    private List<PinResponse> pins = List.of();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinType() {
        return pinType;
    }

    public void setPinType(String pinType) {
        this.pinType = pinType;
    }

    public List<PinResponse> getPins() {
        return pins;
    }

    public void setPins(List<PinResponse> pins) {
        this.pins = pins;
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
}
