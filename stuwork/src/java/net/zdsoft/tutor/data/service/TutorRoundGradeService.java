package net.zdsoft.tutor.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.tutor.data.entity.TutorRoundGrade;

/**
 * @author yangsj  2017年9月11日下午8:19:31
 */
public interface TutorRoundGradeService extends BaseService<TutorRoundGrade,String> {

	/**
	 * @param unitId
	 * @return
	 */
	List<TutorRoundGrade> findByUnitId(String unitId);

	/**
	 * @param roundId
	 * @return
	 */
	List<TutorRoundGrade> findByRoundId(String roundId);

	/**
	 * @param tutorRoundId
	 */
	void deleteByRoundId(String roundId);

	/**
	 * @param array
	 * @param id
	 * @return
	 */
	List<TutorRoundGrade> findbyGradeIdsAndUnitId(String[] gradeIds, String id);

	/**
	 * @param array
	 * @return
	 */
	List<TutorRoundGrade> findbyGradeIds(String... gradeIds);


	

}
