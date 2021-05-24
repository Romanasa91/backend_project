package twins.data;

import javax.persistence.*;
import java.util.Date;


/*
       ITEM_TABLE

IID <PK>     | TYPE         | NAME         | ACTIVE  | TIME      | CREATED_BY   | LOCATION     | ITEM_ATTRIBUTES
=========================================================================================================================
VARCHAR(255) | VARCHAR(255) | VARCHAR(255) | BOOLEAN | TIMESTAMP | VARCHAR(255) | VARCHAR(255) | CLOB
 */

@Entity
@Table(name="ITEMS_TABLE")
public class ItemEntity {

    private String itemId; // = [space:{}, id:{}]
    private String type;
    private String name;
    private Boolean active;
    private Date createdTimestamp;
    private String createdBy; // = [userID:{space:{}, email:{}}]
    private String location; // = [lat:{}, lng:{}]
    private String itemAttributes; // = [key:{Any/JSON}]

    public ItemEntity() {
    }

    @Id
    @Column(name = "IID")
    public String getItemId() {
        return itemId;
    }

    @Column(name = "TYPE")
    public String getType() {
        return type;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    @Column(name = "ACTIVE", columnDefinition = "BIT")
    public Boolean getActive() {
        return active;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="TIME")
    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    @Column(name = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    @Lob
    @Column(name = "ITEM_ATTRIBUTES")
    public String getItemAttributes() {
        return itemAttributes;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setItemAttributes(String itemAttributes) {
        this.itemAttributes = itemAttributes;
    }
}
