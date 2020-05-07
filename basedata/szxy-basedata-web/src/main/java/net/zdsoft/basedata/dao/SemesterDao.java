package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface SemesterDao extends BaseJpaRepositoryDao<Semester, String>, SemesterJdbcDao{
	
	@Query("select distinct acadyear from Semester order by acadyear desc")
	List<String> getAcadyearList();

	/**
	 * @param currentDay
	 * @return
	 * SELECT acadyear,semester FROM base_semester "
            + "WHERE (to_date(?,'YYYY-mm-DD')-work_begin ) >=0 AND (to_date(?,'YYYY-mm-DD') - work_end ) <=0 AND is_deleted = 0";
	 */
	@Query("from Semester WHERE (to_date(?1,'yyyy-MM-dd')-workBegin ) >=0 AND (to_date(?1,'yyyy-MM-dd') - workEnd ) <=0 AND isDeleted = 0")
	Semester findByCurrentDay(String currentDay);

	/**
	 * @param currentDay
	 * @param i
	 * @return 
	 */
	@Query("from Semester WHERE (to_date(?1,'YYYY-mm-DD')- ?2 - workBegin ) >=0 AND (to_date(?1,'YYYY-mm-DD') - ?2 - workEnd ) <=0 AND isDeleted = 0")
	Semester findByCurrentDay(String currentDay, int i);

	/**
	 * @param acadyear
	 * @param semester
	 * @return 
	 */ 
	@Query("from Semester WHERE isDeleted = 0 and acadyear=?1 and semester=?2")
	Semester findByAcadYearAndSemester(String acadyear, Integer semester);

	/**
	 * @return
	 */
	@Query("from Semester WHERE isDeleted = 0 ORDER BY acadyear DESC, semester DESC")
	List<Semester> findSemesters();

	/**
	 * @return
	 */
	@Query("SELECT DISTINCT s FROM Semester as s WHERE isDeleted=0 ORDER BY acadyear DESC")
	List<Semester> findDistinctSemesters();

	/**
	 * @param date
	 * @return
	 * SELECT DISTINCT acadyear FROM base_semester "
        + "WHERE is_deleted = '0' AND work_begin <= ? ORDER BY acadyear DESC
	 */
	@Query("SELECT DISTINCT s FROM Semester as s WHERE isDeleted=0 AND workBegin <= ?1 ORDER BY acadyear DESC")
	List<Semester> findSemestersByDate(Date date);
	
	@Query("SELECT DISTINCT s FROM Semester as s WHERE  ((semesterBegin <= ?1 and semesterEnd >= ?1) or (semesterBegin >= ?1 )) AND isDeleted = 0 order by acadyear DESC, semester DESC ")
	List<Semester> findListByDate(Date date);
	
}
