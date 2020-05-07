package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="eclasscard_full_objall")
public class EccFullObjAll  extends EccTaskEntity{
	private static final long serialVersionUID = 1L;

	private String unitId;
	private String userId;
	private String objectId;
	private String type;
	private boolean lockScreen;
	private Integer sendType;
	
	@Transient
	private String objectName;//展示的内容名称
	@Override
	public String fetchCacheEntitName() {
		return "eccFullObjAll";
	}

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

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isLockScreen() {
		return lockScreen;
	}

	public void setLockScreen(boolean lockScreen) {
		this.lockScreen = lockScreen;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

}
