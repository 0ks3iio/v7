package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;


import net.zdsoft.framework.entity.BaseEntity;

/**
 *  选课内关联
 */
@Entity
@Table(name = "newgkelective_choice_relation")
public class NewGkChoRelation extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	private String unitId;
	private String choiceId;
	private String objectType;
	private String objectValue;
	private Date creationTime;
	private Date modifyTime;
	private String objectTypeVal;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(String choiceId) {
		this.choiceId = choiceId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectValue() {
		return objectValue;
	}

	public void setObjectValue(String objectValue) {
		this.objectValue = objectValue;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkChoRelation";
	}

	public String getObjectTypeVal() {
		return objectTypeVal;
	}

	public void setObjectTypeVal(String objectTypeVal) {
		this.objectTypeVal = objectTypeVal;
	}
	

}
