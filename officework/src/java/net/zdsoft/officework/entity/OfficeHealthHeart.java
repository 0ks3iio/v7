package net.zdsoft.officework.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_health_heart")
public class OfficeHealthHeart extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String serialNumber;
	private String wristbandId;
	private String ownerId;
	private String ownerType;
	private String kinematic;//运动系数，为16进制2位字符
	private int  heartValue;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getWristbandId() {
		return wristbandId;
	}

	public void setWristbandId(String wristbandId) {
		this.wristbandId = wristbandId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	public String getKinematic() {
		return kinematic;
	}

	public void setKinematic(String kinematic) {
		this.kinematic = kinematic;
	}

	public int getHeartValue() {
		return heartValue;
	}

	public void setHeartValue(int heartValue) {
		this.heartValue = heartValue;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "officeHealthHeart";
	}
}