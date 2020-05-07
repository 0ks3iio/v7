package net.zdsoft.eclasscard.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceGatePeriod;
import net.zdsoft.framework.entity.Pagination;

public interface EccAttenceGatePeriodService extends BaseService<EccAttenceGatePeriod, String> {

//	public List<EccAttenceGatePeriod> findListByWeek(String acadyear,String semester,int week,int weekNums);
	
	public void saveAndCheck(String unitId,EccAttenceGatePeriod eccAttenceGatePeriod,
			String[] gradeCodes);

	public List<EccAttenceGatePeriod> findList(String unitId, String gradeCode, Integer classify,
			Pagination page);

	public void deleteById(String id);

}
