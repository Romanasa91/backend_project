package twins.boundaries;

import twins.boundaries.id.ItemId;
import twins.boundaries.id.OperationId;
import twins.boundaries.id.UserId;

import java.util.Date;
import java.util.HashMap;

public class OperationBoundary
{
    private OperationId operationId; // = [space:{}, id:{}]
    private String type;
    private HashMap<String, ItemId> item; // = [itemID:{space:{}, id:{}}]
    private Date createdTimestamp;
    private HashMap<String, UserId> invokedBy; // = [userID:{space:{}, email:{}}]
    private HashMap<String, Object> operationAttributes; // = [key:{Any/JSON}]

    public OperationBoundary() {}

    public OperationId getOperationId() {
        return operationId;
    }

    public void setOperationId(OperationId operationId) {
        this.operationId = operationId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, ItemId> getItem() {
        return item;
    }

    public void setItem(HashMap<String, ItemId> item) {
        this.item = item;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public HashMap<String, UserId> getInvokedBy() {
        return invokedBy;
    }

    public void setInvokedBy(HashMap<String, UserId> invokedBy) {
        this.invokedBy = invokedBy;
    }

    public HashMap<String, Object> getOperationAttributes() {
        return operationAttributes;
    }

    public void setOperationAttributes(HashMap<String, Object> operationAttributes) {
        this.operationAttributes = operationAttributes;
    }
}
