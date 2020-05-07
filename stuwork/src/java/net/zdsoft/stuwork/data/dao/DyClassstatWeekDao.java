package net.zdsoft.stuwork.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyClassstatWeek;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
 
public interface DyClassstatWeekDao extends BaseJpaRepositoryDao<DyClassstatWeek, String>{
	
	public DyClassstatWeek findBySchoolIdAndAcadyearAndSemesterAndClassIdAndWeek(
			String schoolId, String acadyear, String semester, String classId, int week);
	
	public List<DyClassstatWeek> findBySchoolIdAndAcadyearAndSemesterAndWeek(
			String schoolId, String acadyear, String semester, int week);
	
	@Modifying@Transactional
	@Query("delete from DyClassstatWeek where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week = ?4")
	public void deleteByWeek(String schoolId, String acadyear, String semester,
			int week);

	/**
	 * @param schoolId
	 * @param acadyear
	 * @param semester
	 * @param weeks
	 * @param classIds
	 * @return
	 */
	@Query("From DyClassstatWeek where schoolId = ?1 and acadyear = ?2 and semester = ?3 and week in ?4 and classId in ?5")
	public List<DyClassstatWeek> findRankingList(String schoolId, String acadyear, String semester, Integer[] weeks,
			String[] classIds);
	
}
