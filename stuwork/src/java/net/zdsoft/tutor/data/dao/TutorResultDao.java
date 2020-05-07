package net.zdsoft.tutor.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorResult;

/**
 * @author yangsj  2017年9月11日下午8:12:41
 */
public interface TutorResultDao extends BaseJpaRepositoryDao<TutorResult, String> {
    
	String SQL_AFTER=" and state= 0  ";
	/**
	 * @param unitId
	 * @return
	 */
	@Query("From TutorResult  Where unitId = ?1 "+SQL_AFTER)
	List<TutorResult> findByUnitId(String unitId);
	/**
	 * @param tutorRoundId
	 */
	@Modifying
	@Query("delete from TutorResult where roundId=?1")
	void deleteByRoundId(String tutorRoundId);
	/**
	 * @param tid
	 * @return
	 */
	@Query("From TutorResult  Where teacherId = ?1 "+SQL_AFTER)
	List<TutorResult> findByTeacherId(String tid);
	/**
	 * @param tutorRoundId
	 * @return
	 */
	@Query("From TutorResult  Where roundId = ?1 "+SQL_AFTER)
	List<TutorResult> findByRoundId(String tutorRoundId);
	/**
	 * @param studentId
	 * @return
	 */
	@Query("From TutorResult  Where studentId = ?1 "+SQL_AFTER)
	TutorResult findByStudentId(String studentId);
	/**
	 * @param studentId
	 * @return
	 */
	@Query("From TutorResult  Where studentId = ?1 and state= 1")
	public List<TutorResult> findByStudentIdDel(String studentId);
	
	@Query("From TutorResult  Where teacherId in (?1) "+SQL_AFTER)
	public List<TutorResult> findByTeacherIds(String[] tids);
	
	@Query("From TutorResult  Where studentId in (?1) "+SQL_AFTER)
	public List<TutorResult> findByStudentIds(String[] studentIds);
	
	@Modifying
	@Query("update TutorResult set teacherId = ?2, roundId = ?3 , modifyTime = ?4 where studentId in (?1)")
	public void updateTutor(String[] updateStuIds, String teacherId,String tutorId, Date modifyTime);
	
	@Modifying
	@Query("update TutorResult set state = ?1  where roundId in (?2)")
	void updateStateByRoundId(Integer state, String[] roundIds);
	
	/**
	@Query("From TutorResult  Where studentId = ?1 and sction = ?2 "+SQL_AFTER)
	TutorResult findByStudentIdAndSection(String studentId, Integer section);
	@Query("From TutorResult  Where studentId = ?1 and sction = ?2  and state= 1")
	List<TutorResult> findByStudentIdDelAndSection(String studentId,
			Integer section);
	@Query("From TutorResult  Where unitId = ?1 and sction = ?2 "+SQL_AFTER)
	List<TutorResult> findByUnitIdAndSection(String unitId, Integer section);
     */
}
