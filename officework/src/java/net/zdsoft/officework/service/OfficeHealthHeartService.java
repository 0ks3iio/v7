package net.zdsoft.officework.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.officework.entity.OfficeHealthData;
import net.zdsoft.officework.entity.OfficeHealthHeart;


public interface OfficeHealthHeartService extends BaseService<OfficeHealthHeart, String> {

	public void dealTeacherHeartData(String schoolId, String serialNumber,
			Map<String, String> teaCardMap, List<OfficeHealthData> heartDatas);
	
	
}