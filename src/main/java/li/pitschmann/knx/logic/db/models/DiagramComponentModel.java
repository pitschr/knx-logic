package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;
import li.pitschmann.knx.logic.uid.UID;

/**
 * Database model for diagram components
 *
 * @author PITSCHR
 */
public final class DiagramComponentModel extends Model {
    private int diagramId;
    private int componentId;
    private UID componentUid;
    private int positionX;
    private int positionY;

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

    public int getComponentId() {
        return componentId;
    }

    public UID getComponentUid() {
        return componentUid;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("diagramId", diagramId) //
                .add("componentId", componentId) //
                .add("componentUid", componentUid) //
                .add("positionX", positionX) //
                .add("positionY", positionY) //
                .toString();
    }

    /**
     * Creates a new {@link Builder} instance for {@link DiagramComponentModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private int diagramId;
        private int componentId;
        private UID componentUid;
        private int positionX;
        private int positionY;

        public Builder diagramId(final int diagramId) {
            this.diagramId = diagramId;
            return this;
        }

        public Builder componentId(final int componentId) {
            this.componentId = componentId;
            return this;
        }

        public Builder componentUid(final UID componentUid) {
            this.componentUid = componentUid;
            return this;
        }

        public Builder position(final int positionX, final int positionY) {
            this.positionX = positionX;
            this.positionY = positionY;
            return this;
        }

        public DiagramComponentModel build() {
            final var model = new DiagramComponentModel();
            model.diagramId = this.diagramId;
            model.componentId = this.componentId;
            model.componentUid = this.componentUid;
            model.positionX = this.positionX;
            model.positionY = this.positionY;

            return model;
        }
    }
}
