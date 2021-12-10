package experimental.api.v1.json;

import java.util.List;

public class CreateNewPlanRequest {
    private String name;
    private String description;
    private List<String> componentUids;

    public CreateNewPlanRequest(String name, String description, List<String> componentUids) {
        this.name = name;
        this.description = description;
        this.componentUids = componentUids;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getComponentUids() {
        return componentUids;
    }
}
