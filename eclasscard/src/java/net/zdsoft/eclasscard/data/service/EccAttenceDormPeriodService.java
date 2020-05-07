package net.zdsoft.eclasscard.data.service;

import java.text.ParseException;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.entity.EccAttenceDormPeriod;
import net.zdsoft.framework.entity.Pagination;

public interface EccAttenceDormPeriodService extends BaseService<EccAttenceDormPeriod, String> {

	public List<EccAttenceDormPeriod> findList(String unitId,String gradeCode,Pagination page);
	
	public List<EccAttenceDormPeriod> findInNowTime(String unitId);

	public void saveAndCheck(String unitId,EccAttenceDormPeriod eccAttenceDormPeriod,String[] gradeCodes) throws ParseException;

	public void deleteDormPeriod(String id);
	
	public List<EccAttenceDormPeriod> findByIdsOrderBy(String[] ids);

}
