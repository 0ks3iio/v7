package net.zdsoft.tutor.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;

/**
 * @author yangsj  2017年9月11日下午8:14:48
 */
public interface TutorRoundTeacherDao extends BaseJpaRepositoryDao<TutorRoundTeacher, String> {

	/**
	 * @param unitId
	 * @return
	 */
	@Query("From TutorRoundTeacher  Where unitId = ?1 ")
	List<TutorRoundTeacher> findByUnitId(String unitId);

	/**
	 * @param tutorRoundId
	 */
	@Modifying
	@Query("delete from TutorRoundTeacher where roundId =?1")
	void deleteByRoundId(String tutorRoundId);

	/**
	 * @param tutorRoundId
	 * @return
	 */
	@Query("From TutorRoundTeacher  Where roundId = ?1 ")
	List<TutorRoundTeacher> findByRoundId(String tutorRoundId);

	/**
	 * @param tutorRoundId
	 * @param teacherId
	 * @return
	 */
	@Query("From TutorRoundTeacher  Where roundId = ?1 and teacherId = ?2")
	TutorRoundTeacher findByRoundAndTeaId(String tutorRoundId, String teacherId);

	/**
	 * @param tutorRoundId
	 * @param teacherId
	 */
	@Modifying
	@Query("delete from TutorRoundTeacher where roundId =?1 and teacherId = ?2 ")
	void deleteByRoundAndTeaId(String tutorRoundId, String teacherId);

}
