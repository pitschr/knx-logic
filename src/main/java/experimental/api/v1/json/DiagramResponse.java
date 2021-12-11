package experimental.api.v1.json;

import li.pitschmann.knx.logic.diagram.Diagram;

public final class DiagramResponse {
    private final String uid;
    private final String name;
    private final String description;

    private DiagramResponse(final String uid, final String name, final String description) {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    public static DiagramResponse from(final Diagram diagram) {
        return new DiagramResponse(
                diagram.getUid().toString(),
                diagram.getName(),
                diagram.getDescription()
        );
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
