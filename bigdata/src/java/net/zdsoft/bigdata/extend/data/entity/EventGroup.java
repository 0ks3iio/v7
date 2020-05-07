package net.zdsoft.bigdata.extend.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "bg_event_group")
public class EventGroup extends BaseEntity<String>{

	private static final long serialVersionUID = 4226633629475862851L;

	@Override
	public String fetchCacheEntitName() {
		return "eventGroup";
	}
	
	private String unitId;
	
	private String userId;
	
	private String groupName;
	
	private int orderId;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
}
