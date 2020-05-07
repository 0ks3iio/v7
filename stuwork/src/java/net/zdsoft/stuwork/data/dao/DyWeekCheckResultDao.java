package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckResult;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyWeekCheckResultDao extends BaseJpaRepositoryDao<DyWeekCheckResult, String>{
	@Query("From DyWeekCheckResult where schoolId = ?1 and itemId = ?2 and acadyear = ?3 and semester = ?4")
	public List<DyWeekCheckResult> findByItemId(String unitId, String itemId,
			String acadyear, String semester);
	
	@Query("From DyWeekCheckResult where schoolId = ?1 and acadyear = ?2 and semester = ?3 and checkDate = ?4 and classId = ?5 and itemId in ?6 ")
	public List<DyWeekCheckResult> findByCheckDateAndInItemIds(String unitId,
			String acadyear, String semester, Date date, String classId, String[] itemIds);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckResult where id in ?1")
	public void deleteByIds(String[] ids);
	
	@Query("From DyWeekCheckResult where schoolId = ?1 and acadyear = ?2 and semester = ?3 and classId = ?4 and checkDate = ?5 ")
	public List<DyWeekCheckResult> findByClassIdAndCheckDate(String unitId,
			String acadyear, String semester, String classId, Date checkDate);
	
	@Query("From DyWeekCheckResult where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and classId in ?5")
	public List<DyWeekCheckResult> findByWeekAndInClassId(String unitId,
			String acadyear, String semester, int week, String[] classIds);
		
}
