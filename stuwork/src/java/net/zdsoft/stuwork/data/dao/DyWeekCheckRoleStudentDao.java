package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyWeekCheckRoleStudent;

public interface DyWeekCheckRoleStudentDao extends BaseJpaRepositoryDao<DyWeekCheckRoleStudent, String>{
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleStudent where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and section = ?5")
	public void deleteByContion(String schoolId, String acadyear, String semester, int week, String section);
	
	@Modifying
	@Query("DELETE FROM DyWeekCheckRoleStudent where roleId in ?1")
	public void deleteByRoleId(String... roleIds);
	
	@Query("From DyWeekCheckRoleStudent where roleId in ?1")
	public List<DyWeekCheckRoleStudent> findByRoleIds(String[] roleIds);
	
	@Query("From DyWeekCheckRoleStudent where schoolId = ?1 and acadyear = ?2 and semester = ?3 and studentId = ?4")
	public List<DyWeekCheckRoleStudent> findByStuId(String unitId, String acadyear, String semester, String stuId);
	
}
