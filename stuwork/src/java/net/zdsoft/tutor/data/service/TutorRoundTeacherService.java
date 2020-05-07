package net.zdsoft.tutor.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.tutor.data.entity.TutorRoundTeacher;

/**
 * @author yangsj  2017年9月11日下午8:20:09
 */
public interface TutorRoundTeacherService extends BaseService<TutorRoundTeacher, String> {

	/**
	 * @param unitId
	 * @return
	 */
	List<TutorRoundTeacher> findByUnitId(String unitId);

	/**
	 * @param tutorRoundId
	 */
	void deleteByRoundId(String tutorRoundId);

	/**
	 * @param tutorRoundId
	 */
	List<TutorRoundTeacher> findByRoundId(String tutorRoundId);

	/**
	 * @param tutorRoundId
	 * @param teacherId
	 * @return
	 */
	TutorRoundTeacher findByRoundAndTeaId(String tutorRoundId, String teacherId);

	/**
	 * @param tutorRoundId
	 * @param teacherId
	 */
	void deleteByRoundAndTeaId(String tutorRoundId, String teacherId);

	/**
	 * @param tutorId
	 * @param unitId
	 * @param s
	 * @param array
	 * @return
	 */
	List<TutorRoundTeacher> findByRidAndTeaIdIn(String tutorId, String unitId, Pagination pagination, String... teaids);

}
