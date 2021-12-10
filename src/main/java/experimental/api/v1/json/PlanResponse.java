package experimental.api.v1.json;

import experimental.Plan;
import li.pitschmann.knx.logic.pin.Pin;

import java.util.List;
import java.util.stream.Collectors;

public class PlanResponse {
    private final int id;
    private final String name;
    private final String description;
    private final List<ComponentResponse> components;

    private PlanResponse(int id, String name, String description, final List<ComponentResponse> components) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.components = components;
    }

    public static PlanResponse from(final Plan plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getName(),
                plan.getDescription(),
                plan.getComponents().stream().map(ComponentResponse::from).collect(Collectors.toUnmodifiableList())
        );
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ComponentResponse> getComponents() {
        return components;
    }
}
