package net.zdsoft.officework.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_health_sleep")
public class OfficeHealthSleep extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String serialNumber;
	private String wristbandId;
	private String ownerId;
	private String ownerType;
	private String type;//57:午睡  58晚睡
	private int  sleepValue;
	private int  sleepEffectValue;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSleepValue() {
		return sleepValue;
	}

	public void setSleepValue(int sleepValue) {
		this.sleepValue = sleepValue;
	}

	public int getSleepEffectValue() {
		return sleepEffectValue;
	}

	public void setSleepEffectValue(int sleepEffectValue) {
		this.sleepEffectValue = sleepEffectValue;
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