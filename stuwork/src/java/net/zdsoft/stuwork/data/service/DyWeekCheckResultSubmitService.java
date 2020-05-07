package net.zdsoft.stuwork.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;

/**
 * dy_week_check_result_submit
 * @author 
 * 
 */
public interface DyWeekCheckResultSubmitService extends BaseService<DyWeekCheckResultSubmit, String>{

	public List<DyWeekCheckResultSubmit> findByRoleType(String schoolId, String acadyear, String semester, String roleType);

	public void saveSub(DyWeekCheckResultSubmit sub);
	
	public DyWeekCheckResultSubmit findByRoleTypeAndCheckDate(String schoolId,
			String acadyear, String semester, String roleType,Date checkDate);

	public List<DyWeekCheckResultSubmit> findByWeek(String unitId,
			String acadyear, String semester, int week);
	
}