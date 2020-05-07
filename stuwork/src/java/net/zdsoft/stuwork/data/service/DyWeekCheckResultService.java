package net.zdsoft.stuwork.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.dto.DyWeekCheckResultDto;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;

/**
 * dy_week_check_result
 * @author 
 * 
 */
public interface DyWeekCheckResultService extends BaseService<DyWeekCheckResult, String>{
	
	public boolean checkUseItem(String unitId, String itemId, String acadyear, String semester);
	
	public List<DyWeekCheckResultDto> findByItemList(String unitId, String acadyear,
			String semester, String classId, String dutyDate);
	
	public List<DyWeekCheckResult> findByClassIdAndCheckDate(String unitId, String acadyear,
			String semester, String classId, Date checkDate);
	
	public void saveDto(DyWeekCheckResultDto dto);

	public List<DyWeekCheckResult> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds);
	
	public List<DyWeekCheckResult> findByDateAndInClassId(String unitId,
			String acadyear, String semester, Date checkDate, String[] classIds);
	
}