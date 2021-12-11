package experimental.api.v1.json;

import li.pitschmann.knx.logic.components.Component;
import li.pitschmann.knx.logic.connector.Connector;
import li.pitschmann.knx.logic.connector.InputConnectorAware;
import li.pitschmann.knx.logic.connector.OutputConnectorAware;

import java.util.List;
import java.util.stream.Collectors;

public final class ComponentResponse {
    private final String uid;
    private final String className;
    private final List<ConnectorResponse> inputs;
    private final List<ConnectorResponse> outputs;

    private ComponentResponse(final String uid, final String className, final List<Connector> inputs, final List<Connector> outputs) {
        this.uid = uid;
        this.className = className;
        this.inputs = inputs.stream()
                .map(ConnectorResponse::from).collect(Collectors.toUnmodifiableList());
        this.outputs = outputs.stream()
                .map(ConnectorResponse::from).collect(Collectors.toUnmodifiableList());
    }

    public static ComponentResponse from(final Component component) {
        final List<Connector> inputConnectors;
        if (component instanceof InputConnectorAware) {
            inputConnectors = ((InputConnectorAware) component).getInputConnectors();
        } else {
            inputConnectors = List.of();
        }

        final List<Connector> outputConnectors;
        if (component instanceof OutputConnectorAware) {
            outputConnectors = ((OutputConnectorAware) component).getOutputConnectors();
        } else {
            outputConnectors = List.of();
        }

        return new ComponentResponse(
                component.getUid().toString(),
                component.getWrappedObject().getClass().getName(),
                inputConnectors,
                outputConnectors
        );
    }

    public String getUid() {
        return uid;
    }

    public String getClassName() {
        return className;
    }

    public List<ConnectorResponse> getInputs() {
        return inputs;
    }

    public List<ConnectorResponse> getOutputs() {
        return outputs;
    }
}
