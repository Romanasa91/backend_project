package twins.boundaries;

import twins.boundaries.id.ItemId;
import twins.boundaries.id.UserId;

import java.util.Date;
import java.util.HashMap;

public class ItemBoundary {

	private ItemId itemId; // = [space:{}, id:{}]
	private String type;
	private String name;
	private Boolean active;
	private Date createdTimestamp;
	private HashMap<String, UserId> createdBy; // = [userID:{space:{}, email:{}}]
	private HashMap<String, Double> location; // = [lat:{},lng:{}]
	private HashMap<String, Object> itemAttributes;// = [key:{Any/JSON}]

	public ItemBoundary() {}

	public ItemId getItemId() {
		return itemId;
	}

	public void setItemId(ItemId itemId) {
		this.itemId = itemId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Date createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public HashMap<String, UserId> getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(HashMap<String, UserId> createdBy) {
		this.createdBy = createdBy;
	}

	public HashMap<String, Double> getLocation() {
		return location;
	}

	public void setLocation(HashMap<String, Double> location) {
		this.location = location;
	}

	public HashMap<String, Object> getItemAttributes() {
		return itemAttributes;
	}

	public void setItemAttributes(HashMap<String, Object> itemAttributes) {
		this.itemAttributes = itemAttributes;
	}
}
