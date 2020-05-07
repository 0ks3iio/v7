package net.zdsoft.officework.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="office_health_device")
public class OfficeHealthDevice extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String name;
	private String serialNumber;
	private String type;
	private int flag;//进出校设备；1进  2出    0为不标识进出
	
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	@Override
	public String fetchCacheEntitName() {
		return "officeHealthDevice";
	}
}