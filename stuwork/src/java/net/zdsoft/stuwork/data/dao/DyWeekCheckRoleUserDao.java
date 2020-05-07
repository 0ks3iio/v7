package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleUser;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DyWeekCheckRoleUserDao extends BaseJpaRepositoryDao<DyWeekCheckRoleUser, String>{
	
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and userId=?2 and acadyear=?3 and semester=?4 ")
	public List<DyWeekCheckRoleUser> findByUserId(String unitId, String userId,
			String acadyear, String semester);
	
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and userId = ?2 and acadyear=?3 and semester=?4 and roleType=?5 order by dutyDate,week,day,section")
	public List<DyWeekCheckRoleUser> findByRoleId(String unitId, String roleId,
			String acadyear, String semester, String roleType);
	
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and classId = ?2 and acadyear=?3 and semester=?4 and roleType=?5 order by dutyDate,week,day,section")
	public List<DyWeekCheckRoleUser> findByClassId(String unitId, String classId,
			String acadyear, String semester, String roleType);
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and roleType=?2 and userId = ?3 ")
	public DyWeekCheckRoleUser findByRoleTypeAndUserId(String unitId,
			String roleType, String userId);
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and roleType='01'")
	public List<DyWeekCheckRoleUser> findAdminByUnitId(String unitId);
	
	@Modifying
	@Query("delete from DyWeekCheckRoleUser where schoolId = ?1 and roleType = ?2 ")
	public void deleteByRoleType(String schoolId, String roleType);
	
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and roleType=?2 and acadyear = ?3 and semester = ?4 ORDER BY SECTION")
	public List<DyWeekCheckRoleUser> findByRoleType(String unitId,
			String roleType, String acadyear, String semester);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleUser where schoolId = ?1 and roleType = ?2 and acadyear = ?3 and semester = ?4")
	public void deleteByRoleTypeAndAcadyear(String schoolId, String roleType,
			String acadyear, String semester);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleUser where schoolId = ?1 and roleType = ?2 and acadyear = ?3 and semester = ?4 and grade = ?5")
	public void deleteByRoleTypeAndAcadyearAndGrade(String schoolId,
			String roleType, String acadyear, String semester, String gradeId);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleUser where schoolId = ?1 and roleType = ?2 and acadyear = ?3 and semester = ?4 and section = ?5")
	public void deleteByRoleTypeAndAcadyearAndSection(String schoolId,
			String roleType, String acadyear, String semester, String section);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleUser where schoolId = ?1 and roleType = ?2 and acadyear = ?3 and semester = ?4 and week = ?5 and section = ?6")
	public void deleteByClass(String schoolId, String roleType,
			String acadyear, String semester, int week, String section);
	@Query("From DyWeekCheckRoleUser where schoolId=?1 and roleType='02' and dutyDate = ?2")
	public List<DyWeekCheckRoleUser> findCheckTeacher(String unitId, Date date);

	@Query("From DyWeekCheckRoleUser where schoolId=?1 and userId=?2 and roleType='02' and dutyDate = ?3")
	public List<DyWeekCheckRoleUser> findCheckTeacherByUserId(String unitId,String userId,
															  Date dutyDate);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleUser where schoolId = ?1 and roleType = '02' and dutyDate = ?2")
	public void deleteCheckTeacher(String unitId, Date date);
	
	@Modifying
	@Query("DELETE  from DyWeekCheckRoleUser   where  id in ?1")
	public void deleteByInIds(String[] ids);
}
