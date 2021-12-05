package experimental.api.v1.json;

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentResponse {
    private String uid;
    private String className;
    private List<ConnectorResponse> inputs = List.of();
    private List<ConnectorResponse> outputs = List.of();

    private ComponentResponse(final String uid, final String className, final List<ConnectorResponse> inputs, final List<ConnectorResponse> outputs) {
        this.uid = uid;
        this.className = className;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static ComponentResponse from(final Component component) {
        final List<ConnectorResponse> inputConnectorResponses;
        if (component instanceof InputConnectorAware) {
            inputConnectorResponses = ((InputConnectorAware) component).getInputConnectors().stream()
                    .map(ConnectorResponse::from).collect(Collectors.toUnmodifiableList());
        } else {
            inputConnectorResponses = List.of();
        }

        final List<ConnectorResponse> outputConnectorResponses;
        if (component instanceof OutputConnectorAware) {
            outputConnectorResponses = ((OutputConnectorAware) component).getOutputConnectors().stream()
                    .map(ConnectorResponse::from).collect(Collectors.toUnmodifiableList());
        } else {
            outputConnectorResponses = List.of();
        }

        return new ComponentResponse(
                component.getUid().toString(),
                component.getWrappedObject().getClass().getName(),
                inputConnectorResponses,
                outputConnectorResponses
        );
    }

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
