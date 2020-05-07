package net.zdsoft.officework.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_health_data")
public class OfficeHealthData extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String serialNumber;
	private String wristbandId;
	private int  dataValue;
	private String type;
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
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

	public int getDataValue() {
		return dataValue;
	}

	public void setDataValue(int dataValue) {
		this.dataValue = dataValue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
		return "officeHealthDevice";
	}
}