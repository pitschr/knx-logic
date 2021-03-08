package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;

/**
 * Database model for diagram
 *
 * @author PITSCHR
 */
public final class DiagramModel extends Model {
    private String name;
    private String description;

    /**
     * Creates a new {@link Builder} instance
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("name", name) //
                .add("description", description) //
                .toString();
    }

    /**
     * Creates a new {@link Builder} instance for {@link DiagramModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private String name;
        private String description;

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder description(final String description) {
            this.description = description;
            return this;
        }

        public DiagramModel build() {
            final var model = new DiagramModel();
            model.name = this.name;
            model.description = this.description;
            return model;
        }
    }
}
