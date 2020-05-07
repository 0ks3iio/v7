package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "dy_business_option")
public class DyBusinessOption extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String businessType;
	private int orderId;
	private String optionName;
	private int hasScore;
	private int isCustom;
	private float score;
	
	

	public String getUnitId() {
		return unitId;
	}



	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}



	public String getBusinessType() {
		return businessType;
	}



	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}



	public int getOrderId() {
		return orderId;
	}



	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}



	public String getOptionName() {
		return optionName;
	}



	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}



	public int getHasScore() {
		return hasScore;
	}



	public void setHasScore(int hasScore) {
		this.hasScore = hasScore;
	}



	public int getIsCustom() {
		return isCustom;
	}



	public void setIsCustom(int isCustom) {
		this.isCustom = isCustom;
	}



	public float getScore() {
		return score;
	}



	public void setScore(float score) {
		this.score = score;
	}



	@Override
	public String fetchCacheEntitName() {
		return "DyBusinessOption";
	}

}
