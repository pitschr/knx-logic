package experimental.api.v1.json;

import li.pitschmann.knx.core.utils.Strings;

/**
 * JSON Payload Request to create a diagram
 * <p>
 * <pre>{@code
 *   {
 *     "name": "New Diagram",
 *     "description": "New Diagram Description"
 *   }
 * }</pre>
 */
public final class DiagramRequest {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("name", name)
                .add("description", description)
                .toString();
    }
}
