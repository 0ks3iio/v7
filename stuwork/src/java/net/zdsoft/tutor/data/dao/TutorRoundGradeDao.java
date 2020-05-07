package net.zdsoft.tutor.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorRoundGrade;

/**
 * @author yangsj  2017年9月11日下午8:14:06
 */
public interface TutorRoundGradeDao extends BaseJpaRepositoryDao<TutorRoundGrade, String> {

	/**
	 * @param unitId
	 * @return
	 */
	@Query("From TutorRoundGrade  Where unitId = ?1 ")
	List<TutorRoundGrade> findByUnitId(String unitId);

	/**
	 * @param roundId
	 * @return
	 */
	@Query("From TutorRoundGrade  Where roundId = ?1 ")
	List<TutorRoundGrade> findByRoundId(String roundId);

	/**
	 * @param tutorRoundId
	 */
	@Modifying
	@Query("delete from TutorRoundGrade where roundId=?1")
	void deleteByRoundId(String roundId);

	/**
	 * @param gradeIds
	 * @param roundId
	 * @return
	 */
	@Query("From TutorRoundGrade  Where gradeId in ?1 and unitId = ?2 ")
	List<TutorRoundGrade> findbyGradeIdsAndUnitId(String[] gradeIds, String unitId);

	/**
	 * @param gradeIds
	 * @return
	 */
	@Query("From TutorRoundGrade  Where gradeId in ?1 ")
	List<TutorRoundGrade> findbyGradeIds(String[] gradeIds);

}
