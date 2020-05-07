package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResultSubmit;

public interface DyWeekCheckResultSubmitDao extends BaseJpaRepositoryDao<DyWeekCheckResultSubmit, String>{
	@Query("From DyWeekCheckResultSubmit where schoolId=?1 and acadyear =?2 and semester = ?3 and roleType = ?4 ")
	public List<DyWeekCheckResultSubmit> findByRoleType(String schoolId,
			String acadyear, String semester, String roleType);
	
	@Query("From DyWeekCheckResultSubmit where schoolId=?1 and acadyear =?2 and semester = ?3 and roleType = ?4 and checkDate = ?5")
	public DyWeekCheckResultSubmit findByRoleTypeAndCheckDate(String schoolId,
			String acadyear, String semester, String roleType,Date checkDate);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckResultSubmit where schoolId=?1 and acadyear =?2 and semester = ?3 and roleType = ?4 and checkDate = ?5 ")
	public void deleteByCheckDateAndRoleType(String schoolId, String acadyear,
			String semester, String roleType, Date checkDate);
	@Query("From DyWeekCheckResultSubmit where schoolId=?1 and acadyear =?2 and semester = ?3 and week = ?4")
	public List<DyWeekCheckResultSubmit> findByWeek(String unitId,
			String acadyear, String semester, int week);
	
}
