package net.zdsoft.scoremanage.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.SubjectInfo;

public interface SubjectInfoDao extends BaseJpaRepositoryDao<SubjectInfo, String>{

	@Query("From SubjectInfo where examId = ?1 and rangeType in (?2) order by rangeType,subjectId")
	public List<SubjectInfo> findByExamIdAndRangeType(String examId, String... rangeType);
	
	@Query("From SubjectInfo where examId in (?1) order by rangeType,subjectId")
	public List<SubjectInfo> findByExamIdIn(String... examIds);

	public List<SubjectInfo> findByExamIdAndSubjectId(String examId,
			String subjectId);

	@Query("select s1 from SubjectInfo as s1, ClassInfo as s2 where s1.id=s2.subjectInfoId and s1.examId=?1 and  s2.classId=?2 ")
	public List<SubjectInfo> findByExamIdClassId(String examId,
			String classId);
	@Modifying
	@Query("delete from SubjectInfo where id in (?1)")
	public void deleteAllByIds(String... id);
	
	@Query("From SubjectInfo where examId = ?1")
	public List<SubjectInfo> findByExamId(String examId);
	
	@Query("From SubjectInfo where unitId = ?1 and examId = ?2 and subjectId = ?3")
	public List<SubjectInfo> findByExamIdAndCourseIdAndUnitId(String unitId, String examId, String courseId);

	@Query("select s1 from SubjectInfo as s1, ClassInfo as s2 where s1.id=s2.subjectInfoId and s1.examId=?1 and  s2.schoolId=?2 ")
	public List<SubjectInfo> findByExamIdUnitId(String examId, String unitId);

	public SubjectInfo findByExamIdAndSubjectIdAndRangeType(String examId, String courseId, String gradeCode);
	
}
