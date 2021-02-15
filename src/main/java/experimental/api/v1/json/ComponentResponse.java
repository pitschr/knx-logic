package experimental.api.v1.json;

import java.util.List;

public class ComponentResponse {
    private String uid;
    private String className;
    private List<ConnectorResponse> inputs = List.of();
    private List<ConnectorResponse> outputs = List.of();

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<ConnectorResponse> getInputs() {
        return inputs;
    }

    public void setInputs(List<ConnectorResponse> inputs) {
        this.inputs = inputs;
    }

    public List<ConnectorResponse> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<ConnectorResponse> output) {
        this.outputs = output;
    }
}
