package twins.data;

import javax.persistence.*;
import java.util.Date;


/*
       OPERATIONS_TABLE

OID <PK>      | TYPE          | ITEM          | TIME          | INVOKED_BY   | OPERATION_ATTRIBUTES
===================================================================================================
VARCHAR(255)   | VARCHAR(255)  | VARCHAR(255) | TIMESTAMP     | VARCHAR(255) | CLOB
 */


@Entity
@Table(name="OPERATIONS_TABLE")
public class OperationEntity {

    private String operationId; // = "space;id"
    private String type;
    private String item; // = "space;id"
    private Date createdTimestamp;
    private String  invokedBy; // = "space;email"
    private String operationAttributes; // = [key:{Any/JSON}]


    public OperationEntity(){

    }
    @Id
    @Column(name = "OID")
    public String getOperationId() {
        return operationId;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    @Column(name = "ITEM")
    public String getItem() {
        return item;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="TIME")
    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    @Column(name = "INVOKED_BY")
    public String getInvokedBy() {
        return invokedBy;
    }

    @Lob
    @Column(name = "OPERATION_ATTRIBUTES")
    public String getOperationAttributes() {
        return operationAttributes;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setInvokedBy(String invokedBy) {
        this.invokedBy = invokedBy;
    }

    public void setOperationAttributes(String operationAttributes) {
        this.operationAttributes = operationAttributes;
    }
}



