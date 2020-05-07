package net.zdsoft.officework.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.entity.OfficeHealthData;


public interface OfficeHealthDataService extends BaseService<OfficeHealthData, String> {

	public void saveHealthData(List<OfficeHealthData> healthDatas);
	
}