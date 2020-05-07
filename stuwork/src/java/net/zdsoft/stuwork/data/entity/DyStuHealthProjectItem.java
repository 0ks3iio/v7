package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_stu_health_project_item")
public class DyStuHealthProjectItem extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	
	private String acadyear;
	
	private String semester;
	
	private String itemId;//指标id
	
	private int orderId;
	
	private String itemName;
	
	private String itemUnit;
	

	public String getUnitId() {
		return unitId;
	}


	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}


	public String getAcadyear() {
		return acadyear;
	}


	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}


	public String getSemester() {
		return semester;
	}


	public void setSemester(String semester) {
		this.semester = semester;
	}


	public String getItemId() {
		return itemId;
	}


	public void setItemId(String itemId) {
		this.itemId = itemId;
	}


	public int getOrderId() {
		return orderId;
	}


	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}


	public String getItemName() {
		return itemName;
	}


	public void setItemName(String itemName) {
		this.itemName = itemName;
	}


	public String getItemUnit() {
		return itemUnit;
	}


	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}


	@Override
	public String fetchCacheEntitName() {
		return "getHealthProject";
	}

}
