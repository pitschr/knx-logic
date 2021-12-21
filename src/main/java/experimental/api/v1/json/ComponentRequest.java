package experimental.api.v1.json;

import li.pitschmann.knx.core.utils.Strings;

import java.util.Map;

/**
 * JSON Payload Request for /v1/components
 * <p>
 * <p>
 * <pre>Create Logic Component: {@code
 *    { type: "logic", data: { "class": "my.package.Logic.class" } }
 * }</pre>
 * <p>
 * <pre>Create Variable Inbox Component: {@code
 *    { type: "inbox", event: "VAR", data: { "name": "foobar" } }
 * }</pre>
 * <p>
 * <pre>Create KNX Inbox Component: {@code
 *    { type: "inbox", event: "KNX", data: { "groupAddress": "1/2/3", "dpt": "dpt-1" } }
 * }</pre>
 * <p>
 * <pre>Create Variable Outbox Component: {@code
 *    { type: "outbox", event: "VAR", data: { "name": "foobar" } }
 * }</pre>
 * <p>
 * <pre>Create KNX Outbox Component: {@code
 *    { type: "outbox", event: "KNX", data: { "groupAddress": "1/2/3", "dpt": "dpt-1" } }
 * }</pre>
 */
public final class ComponentRequest {
    private String type;
    private String event;
    private Map<String, String> data = Map.of();

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(final String event) {
        this.event = event;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(final Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return Strings.toStringHelper(this)
                .add("type", type)
                .add("event", event)
                .add("data", data)
                .toString();
    }
}


