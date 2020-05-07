package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;

import org.springframework.data.jpa.repository.Query;

public interface ScoreInfoDao extends BaseJpaRepositoryDao<ScoreInfo, String>, ScoreInfoJdbcDao{

	public List<ScoreInfo> findByExamIdAndStudentIdIn(String examId, String[] studentIds);
	public List<ScoreInfo> findByExamIdAndStudentIdInAndSubjectIdIn(String examId,String[] studentIds, String[] subjectIds);

	public List<ScoreInfo> findByExamIdAndUnitIdAndClassId(String examId,
			String unitId, String classId);

	public List<ScoreInfo> findByExamIdAndUnitIdAndTeachClassId(String examId,
			String unitId, String teachClassId);

	public List<ScoreInfo> findByExamIdAndUnitIdAndClassIdAndSubjectIdIn(
			String examId, String unitId, String classId, String[] subjectIds);


	public List<ScoreInfo> findByExamIdAndUnitIdAndTeachClassIdAndSubjectIdIn(
			String examId, String unitId, String teachClassId, String[] subjectIds);

	public List<ScoreInfo> findByExamId(String examId);

	public List<ScoreInfo> findByExamIdAndUnitIdAndClassIdAndSubjectId(
			String examId, String schoolId, String classId,
			String subjectId);

	public List<ScoreInfo> findByExamIdAndUnitIdAndTeachClassIdAndSubjectId(
			String examId, String schoolId, String teachClassId,
			String subjectId);

	public ScoreInfo findByExamIdAndSubjectIdAndStudentId(String examId,
			String subjectId, String studentId);

	public List<ScoreInfo> findByExamIdAndUnitIdAndStudentIdIn(String examId,
			String unitId, String[] studentIds);
	
	public List<ScoreInfo> findByExamIdAndUnitIdAndStudentIdInAndSubjectIdIn(String examId,
			String unitId, String[] studentIds, String[] subjectIds);

	@Query("From ScoreInfo where examId = ?1 and unitId= ?2 and (teachClassId=?3 or classId=?3) and subjectId in (?4)")
	public List<ScoreInfo> findByExamIdAndUnitIdAndSelectClassIdAndSubjectIdIn(
			String examId, String unitId, String classIdSearch, String[] subjectId);
	@Query("From ScoreInfo where examId = ?1 and unitId= ?2 and (teachClassId=?3 or classId=?3)")
	public List<ScoreInfo> findByExamIdAndUnitIdAndSelectClassId(String examId,
			String unitId, String classIdSearch);
	@Query("From ScoreInfo where examId = ?1 and subjectId in (?2) and scoreStatus = ?3")
	public List<ScoreInfo> findScoreInfoList(String examId, String[] subjectIds, String scoreStatus);
	@Query("From ScoreInfo where examId = ?1 and subjectId in (?2) and scoreStatus = ?3 and unitId = ?4")
	public List<ScoreInfo> findScoreInfoList(String examId, String[] subjectIds, String scoreStatus,String unitId);
	
	@Query("From ScoreInfo where unitId = ?1 and examId in (?2) and inputType=?3 order by studentId,subjectId,score")
	public List<ScoreInfo> findScoreByExamIds(String unitId,String[] examIds,String inputType);
	
	@Query("From ScoreInfo where examId =?1 and inputType=?2 and classId in (?3)")
	public List<ScoreInfo> findByClsIds(String examId, String inputType,String... clsIds);
	
	@Query("From ScoreInfo where unitId =?1 and acadyear=?2 and semester=?3 and examId=?4 and subjectId=?5 and inputType=?6 and classId in (?7)")
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear,
			String semester, String examId, String subjectId,String inputType, String... clsIds);
	
	@Query("From ScoreInfo where unitId =?1 and acadyear=?2 and semester=?3 and examId=?4  and inputType=?5 and classId in (?6)")
	public List<ScoreInfo> findListByClsIds(String unitId, String acadyear,
			String semester, String examId, String inputType, String... clsIds);
	
	/**
	 * 用来根据以下条件获取选修课分数信息
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param examId 考试ID. 是32个0时为选修课的成绩
	 * @param subjectId
	 * @param teachClassId
	 * @return
	 */
	public List<ScoreInfo> findByUnitIdAndExamIdAndSubjectIdAndTeachClassId(
			String unitId, String examId,
			String subjectId, String teachClassId);

	/**
	 * 用来根据以下条件获取选修课分数信息
	 * @param acadyear
	 * @param semester
	 * @param unitId
	 * @param examId 考试ID. 是32个0时为选修课的成绩
	 * @param teachClassIds
	 * @return
	 */
	public List<ScoreInfo> findByUnitIdAndExamIdAndTeachClassIdIn(String unitId, String zeroGuid, String[] teachClassIds);

}
