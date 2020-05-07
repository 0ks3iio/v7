package net.zdsoft.stuwork.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyStuWeekCheckPerformance;

public interface DyStuWeekCheckPerformanceDao extends BaseJpaRepositoryDao<DyStuWeekCheckPerformance, String>{
	@Query("from DyStuWeekCheckPerformance where unitId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4 and studentId in (?5)")
	public List<DyStuWeekCheckPerformance> findByStudentIds(String unitId, String acadyear, String semester, int week, String[] studentIds);
	
	@Query("from DyStuWeekCheckPerformance where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId in (?4)")
	public List<DyStuWeekCheckPerformance> findByStudentIds2(String unitId, String acadyear, String semester, String[] studentIds);
	
    @Modifying
    @Query("delete from DyStuWeekCheckPerformance where acadyear = ?1 and semester = ?2 and week = ?3 and studentId in (?4)")
	public void deleteByStudentIds(String acadyear, String semester, int week, String[] studentIds);
    @Query("from DyStuWeekCheckPerformance where studentId = ?1")
    public List<DyStuWeekCheckPerformance> findByStudentId(String studentId);
    
	public List<DyStuWeekCheckPerformance> findByUnitIdAndGradeIdIn(String unitId, String[] gradeIds);
	
	@Query("from DyStuWeekCheckPerformance where unitId = ?1 and acadyear = ?2 and semester = ?3 and studentId in (?4)")
	public List<DyStuWeekCheckPerformance> findByStudentIds(String unitId, String acadyear, String semester, String[] studentIds);
	
	@Query("from DyStuWeekCheckPerformance where unitId = ?1 and acadyear = ?2 and semester = ?3 ")
	public List<DyStuWeekCheckPerformance> findByUnitIdAnd(String unitId, String acadyear, String semester);
	
	@Query("from DyStuWeekCheckPerformance where unitId = ?1 and acadyear = ?2 and semester = ?3 and week in (?4) and studentId in (?5)")
	public List<DyStuWeekCheckPerformance> findByStudentIds(String unitId, String acadyear, String semester, Integer[] weeks, String[] studentIds);
	
	@Query("from DyStuWeekCheckPerformance where studentId in (?1)")
	public List<DyStuWeekCheckPerformance> findByStudentIds(String[] studentIds);
}
