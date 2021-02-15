package li.pitschmann.knx.logic.db.models;

import li.pitschmann.knx.core.utils.Strings;

/**
 * Database model for {@link li.pitschmann.knx.logic.event.EventKey}
 * holding channel id and the identifier for event
 *
 * @author PITSCHR
 */
public final class EventKeyModel extends Model {
    private int componentId;
    private String channel;
    private String key;

    /**
     * Creates a new {@link Builder} instance for {@link EventKeyModel}
     *
     * @return {@link Builder}
     */
    public static Builder builder() {
        return new Builder();
    }

    public int getComponentId() {
        return componentId;
    }

    public void setComponentId(int componentId) {
        this.componentId = componentId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(final String channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this) //
                .add("componentId", componentId) //
                .add("channel", channel) //
                .add("key", key) //
                .toString();
    }

    /**
     * Builder instance for {@link EventKeyModel}
     *
     * @author PITSCHR
     */
    public static class Builder {
        private int componentId;
        private String channel;
        private String key;

        public Builder componentId(final int componentId) {
            this.componentId = componentId;
            return this;
        }

        public Builder channel(final String event) {
            this.channel = event;
            return this;
        }

        public Builder key(final String key) {
            this.key = key;
            return this;
        }

        public EventKeyModel build() {
            final var model = new EventKeyModel();
            model.componentId = componentId;
            model.channel = this.channel;
            model.key = this.key;
            return model;
        }
    }
}
