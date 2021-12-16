package experimental.api.v1.json;

public final class DiagramRequest {
    private final String name;
    private final String description;

    public DiagramRequest(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
