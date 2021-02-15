package experimental.api.v1.json;

public class UpdateConnectorRequest {
    private String action;
    private Integer index;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
