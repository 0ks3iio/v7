package net.zdsoft.tutor.data.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorRecord;

/**
 * @author yangsj  2017年9月11日下午8:11:37
 */
public interface TutorRecordDao extends BaseJpaRepositoryDao<TutorRecord,String> {
	String SQL_AFTER=" Order By creationTime desc";
	/**
	 * @param teacherIds
	 * @return
	 */
	@Query("From TutorRecord  Where teacherId in ?1 ")
	List<TutorRecord> findByTeacherIds(List<String> teacherIds);

	/**
	 * @param studentIds
	 * @return
	 */
	@Query("From TutorRecord  Where studentId in ?1 "+SQL_AFTER)
	List<TutorRecord> findByStudentIds(String... studentIds);

	/**
	 * @param studentIds
	 */
	@Modifying
	@Query("delete from TutorRecord where studentId in ?1")
	void deleteByStudentIds(List<String> studentIds);

	/**
	 * @param unitId
	 * @param sId
	 * @return
	 */
	@Query("From TutorRecord  Where unitId = ?1 and studentId = ?2 ")
	List<TutorRecord> findByUIdAndSId(String unitId, String sId);

	/**
	 * @param sId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@Query("From TutorRecord  Where studentId = ?1 and acadyear = ?2 and semester = ?3 and isStudentShow = 1 " + SQL_AFTER)
	List<TutorRecord> findBySIdAndSemester(String sId, String acadyear, String semester);
	
	@Query("From TutorRecord  Where studentId = ?1 and acadyear = ?2 and semester = ?3 " + SQL_AFTER)
	List<TutorRecord> findByAllSIdAndSemester(String sId, String acadyear, String semester);
	
	@Modifying
	@Query("update TutorRecord set teacherId = ?1, modifyTime = ?3 where studentId in (?2)")
	public void updateTeacherByStuIds(String teacherId, String[] updateStuIds, Date modifyTime);

	@Query("From TutorRecord  Where teacherId in (?1) and acadyear = ?2 and semester = ?3 ")
	public List<TutorRecord> findByTIdsAndSemester(String[] teacherIds,
			String acadyear, String semester);

	/**
	 * @param id
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@Query("From TutorRecord  Where studentId = ?1 and acadyear = ?2 and semester = ?3 and isFamilyShow = 1 " + SQL_AFTER)
	List<TutorRecord> findByFamIdAndSemester(String id, String acadyear, String semester);


	/**
	 * @param tutorRecordDetailedId
	 */
	@Modifying
	@Query("delete from TutorRecord where detailedId = ?1")
	void deleteByDetailed(String tutorRecordDetailedId);

	/**
	 * @param tutorRecordDetailedId
	 * @param sid
	 * @return
	 */
	@Query("From TutorRecord  Where detailedId = ?1 and studentId = ?2 ")
	TutorRecord findByDetailedIdAndStuId(String tutorRecordDetailedId, String sid);

	/**
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @return
	 */
	@Query("From TutorRecord  Where unitId = ?1 and acadyear = ?2 and semester = ?3 and detailedId != null")
	List<TutorRecord> findByUidAndSemester(String unitId, String acadyear, String semester);

}
