package net.zdsoft.officework.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.entity.OfficeHealthDevice;


public interface OfficeHealthDeviceService extends BaseService<OfficeHealthDevice, String> {
	
	public OfficeHealthDevice findBySerialNumber(String serialNumber);
	
	public List<OfficeHealthDevice> findByType(String type);
	
	public List<OfficeHealthDevice> getByUnitAndType(String unitId,String type);
	
}