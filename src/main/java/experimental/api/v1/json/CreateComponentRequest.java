package experimental.api.v1.json;

import li.pitschmann.knx.core.utils.Strings;

import java.util.Map;

// LOGIC: { type: "logic", data: { "class": "my.package.Logic.class" } }
// INBOX (KNX): { type: "inbox", event: "KNX", data: { "groupAddress": "1/2/3", "dpt": "dpt-1" } }
// INBOX (VAR): { type: "inbox", event: "VAR", data: { "name": "foobar" } }
// OUTBOX (KNX): { type: "outbox", event: "KNX", data: { "groupAddress": "1/2/3", "dpt": "dpt-1" } }
// OUTBOX (VAR): { type: "outbox", event: "VAR", data: { "name": "foobar" } }
public final class CreateComponentRequest {
    private String type;
    private String event;
    private Map<String, String> data = Map.of();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
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


