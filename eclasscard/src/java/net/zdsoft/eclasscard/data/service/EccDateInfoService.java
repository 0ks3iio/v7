package net.zdsoft.eclasscard.data.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.eclasscard.data.dto.EccDateInfoDto;
import net.zdsoft.eclasscard.data.entity.EccDateInfo;

public interface EccDateInfoService extends BaseService<EccDateInfo, String>{
	public List<EccDateInfoDto> getDateList(String schoolId,String grade,String justSunDay);

	public void saveDateInfos(String schoolId,String grade,List<EccDateInfoDto> eccDateInfoDtos);
	
	public Map<String,EccDateInfo> getDateMap(String schoolId,String grade);
	
	public EccDateInfo getByDateGrade(String gradeId,String date);

	public List<EccDateInfo> getDateSchoolId(String unitId,
			String date2StringByDay);

	public List<Date> getInfoDateList(String unitId, String gradeId,
			Date beginDate, Date endDate);
}
