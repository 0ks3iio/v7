package net.zdsoft.tutor.data.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.tutor.data.entity.TutorRecord;

/**
 * @author yangsj  2017年9月11日下午8:17:36
 */
public interface TutorRecordService extends BaseService<TutorRecord, String> {

	/**
	 * @param teacherIds
	 * @return
	 */
	List<TutorRecord> findByTeacherIds(List<String> teacherIds);

	/**
	 * @param studentIds
	 * @return
	 */
	List<TutorRecord> findByStudentIds(String... studentIds);

	/**
	 * @param studentIds
	 * @return
	 */
	void deleteByStudentIds(List<String> studentIds);

	/**
	 * @param unitId
	 * @param id
	 * @return
	 */
	List<TutorRecord> findByUIdAndSId(String unitId, String id);

	/**
	 * @param id
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<TutorRecord> findBySIdAndSemester(String id, String acadyear, String semester);
	/**
	 * 换学生导师记录
	 * @param teacherId
	 * @param updateStuIds
	 * @param modifyTime
	 */
	public void updateTeacherByStuIds(String teacherId, String[] updateStuIds, Date modifyTime);
	/**
	 * 取教师学年学期记录
	 * @param teacherIds
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	public List<TutorRecord> findByTIdsAndSemester(String[] teacherIds, String acadyear, String semester);

	/**
	 * @param id
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<TutorRecord> findByFamIdAndSemester(String id, String acadyear, String semester);

	/**
	 * @param studentId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<TutorRecord> findByAllSIdAndSemester(String studentId, String acadyear, String semester);


	/**
	 * @param tutorRecordDetailedId
	 */
	void deleteByDetailed(String tutorRecordDetailedId);

	/**
	 * @param id
	 * @param sid
	 * @return
	 */
	TutorRecord findByDetailedIdAndStuId(String id, String sid);

	/**
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	List<TutorRecord> findByUidAndSemester(String unitId, String acadyear, String semester);

}
