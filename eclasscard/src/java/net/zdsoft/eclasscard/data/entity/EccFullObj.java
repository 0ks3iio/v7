package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="eclasscard_full_obj")
public class EccFullObj  extends EccTaskEntity{
	private static final long serialVersionUID = 1L;

	private String eccInfoId;
	private String objectId;//展示的内容id
	private String type;
	private boolean lockScreen;
	private String sourceType;//01 单个；02全校
	private String sourceId;//校级班牌，发布的全屏展示id,否则取objectId
	
	@Transient
	private String objectName;//展示的内容名称
	@Override
	public String fetchCacheEntitName() {
		return "eccFullObj";
	}

	public String getEccInfoId() {
		return eccInfoId;
	}

	public void setEccInfoId(String eccInfoId) {
		this.eccInfoId = eccInfoId;
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

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	
}
