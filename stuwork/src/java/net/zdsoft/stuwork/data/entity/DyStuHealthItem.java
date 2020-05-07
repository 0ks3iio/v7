package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dy_stu_health_item")
public class DyStuHealthItem extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId;
	
	private int orderId;//序号
	
	private String itemName;//指标名称
	
	private String itemUnit;//指标单位
	
	@Transient
	private boolean haveSelected;//判断某学年学期是否选择此指标
	
	
	
	public boolean isHaveSelected() {
		return haveSelected;
	}
	public void setHaveSelected(boolean haveSelected) {
		this.haveSelected = haveSelected;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
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
		return "getHealthItem";
	}

}
