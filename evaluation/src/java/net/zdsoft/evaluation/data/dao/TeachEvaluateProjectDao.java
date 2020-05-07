package net.zdsoft.evaluation.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.evaluation.data.entity.TeachEvaluateProject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachEvaluateProjectDao extends BaseJpaRepositoryDao<TeachEvaluateProject,String>{
	
	@Query("From TeachEvaluateProject where unitId = ?1 and acadyear = ?2 and semester = ?3 order by beginTime")
	public List<TeachEvaluateProject> findByUnitIdAndAcadyearAndSemester(String unitId, String acadyear, String semester);
	
	@Query("From TeachEvaluateProject where unitId = ?1 and acadyear = ?2 and semester = ?3 and evaluateType = ?4 and ((beginTime <= ?5 and endTime >= ?5) or (beginTime <= ?6 and endTime >= ?6) or (beginTime > ?5 and endTime < ?6))")
	public List<TeachEvaluateProject> findExist(String unitId, String acadyear,
			String semester, String evaluateType, Date beginTime, Date endTime);
	
	@Query("From TeachEvaluateProject where unitId = ?1 and acadyear = ?2 and semester = ?3 and evaluateType = ?4 order by beginTime")
	public List<TeachEvaluateProject> findByUnitIdAndAcadyearAndSemesterAndEvaluateType(
			String unitId, String acadyear, String semester, String evaluateType);

	@Modifying
	@Query("Update  TeachEvaluateProject set beginTime =?1, endTime = ?2 where id = ?3")
	public void updateDate(Date beginTime,Date endTime,String id);
	
}
