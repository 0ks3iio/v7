package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_place_item")
public class NewGkPlaceItem extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String arrayItemId;
	private String objectId;
	private String placeId;
	/**
	 * 暂时objectId：divideClassId  type 1：行政班 2：教学班 4. 组合固定 伪行政班
	 */
	private String type;

	// private String isNeed;

	//用于页面保存
	@Transient
	private String objectIds;
	@Transient
	private String objectIds2;
	
	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkPlaceItem";
	}

	public String getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(String objectIds) {
		this.objectIds = objectIds;
	}

	public String getObjectIds2() {
		return objectIds2;
	}

	public void setObjectIds2(String objectIds2) {
		this.objectIds2 = objectIds2;
	}

}
