package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;

/**
 * Database model for diagram links
 *
 * @author PITSCHR
 */
public final class DiagramLinkModel extends Model {
    private int diagramId;
    private int pinLinkId;
    private String svgPath;

    /**
     * Creates a new {@link Builder} instance
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public int getDiagramId() {
        return diagramId;
    }

    public int getPinLinkId() {
        return pinLinkId;
    }

    public String getSvgPath() {
        return svgPath;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("diagramId", diagramId) //
                .add("pinLinkId", pinLinkId) //
                .add("svgPath", svgPath) //
                .toString();
    }

    /**
     * Creates a new {@link Builder} instance for {@link DiagramLinkModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private int diagramId;
        private int pinLinkId;
        private String svgPath;

        public Builder diagramId(final int diagramId) {
            this.diagramId = diagramId;
            return this;
        }

        public Builder pinLinkId(final int pinLinkId) {
            this.pinLinkId = pinLinkId;
            return this;
        }

        public Builder svgPath(final String svgPath) {
            this.svgPath = svgPath;
            return this;
        }

        public DiagramLinkModel build() {
            final var model = new DiagramLinkModel();
            model.diagramId = this.diagramId;
            model.pinLinkId = this.pinLinkId;
            model.svgPath = this.svgPath;
            return model;
        }
    }
}
