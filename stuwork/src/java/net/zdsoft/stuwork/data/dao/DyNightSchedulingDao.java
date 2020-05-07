package net.zdsoft.stuwork.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stuwork.data.entity.DyNightScheduling;

/**
 * @author yangsj  2017年11月30日下午3:02:19
 */
public interface DyNightSchedulingDao extends BaseJpaRepositoryDao<DyNightScheduling, String>{

	/**
	 * @param unitId
	 * @param userId
	 * @param queryDate
	 * @return
	 */
	@Query("From DyNightScheduling where unitId = ?1 and teacherId = ?2 and nightTime = ?3")
	List<DyNightScheduling> findByUserAndNightTime(String unitId, String teacherId, Date queryDate);

	/**
	 * @param unitId
	 * @param nightTime
	 * @return
	 */
	@Query("From DyNightScheduling where unitId = ?1 and nightTime = ?2")
	List<DyNightScheduling> findByUserAndNightTime(String unitId, Date nightTime);

	/**
	 * @param teacherId
	 * @param classId
	 * @param queryDate
	 * @return
	 */
	@Query("From DyNightScheduling where unitId = ?1  and classId = ?2 and nightTime = ?3")
	DyNightScheduling findByTeaIdAndClaIdAndTime(String unitId, String classId, Date queryDate);

	/**
	 * @param unitId
	 * @return
	 */
	@Query("From DyNightScheduling where unitId = ?1 ")
	List<DyNightScheduling> findByUnitId(String unitId);

	/**
	 * @param queryDate
	 * @param clazzList
	 */
	@Modifying
	@Query("delete from DyNightScheduling where nightTime = ?1 and classId in ?2")
	void deleteByNightTimeAndClazzs(Date queryDate, String[] clazzList);

	
}
