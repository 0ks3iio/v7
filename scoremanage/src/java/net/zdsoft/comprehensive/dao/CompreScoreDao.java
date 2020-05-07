package net.zdsoft.comprehensive.dao;

import java.util.List;

import net.zdsoft.comprehensive.entity.CompreScore;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CompreScoreDao extends BaseJpaRepositoryDao<CompreScore, String>{

	@Query("from CompreScore where unitId=?1 and examId=?2 and scoremanageType=?3 and subjectId in (?4) order by subjectId,ranking")
	List<CompreScore> findByExamIdSubjectIdIn(String unitId, String examId,String type,
			String[] subjectIds);
	
	@Query("from CompreScore where unitId=?1 and acadyear=?2 and semester=?3 and scoremanageType =?4 and classId in (?5) order by classId")
	List<CompreScore> getFirstList(String unitId,String acadyear,String semester,String scoreType,String[] classIds);

	@Modifying
	@Query("delete from CompreScore where examId = ?1 ")
	void deleteByExamId(String id);

	
	@Query("from CompreScore where unitId=?1 and acadyear=?2 and semester=?3 and scoremanageType=?4 and studentId in (?5) order by subjectId,ranking")
	List<CompreScore> findByStudentIds(String unitId, String acadyear,String semester,String type,
			String[] studentIds);

	@Modifying
	@Query("delete from CompreScore where unitId=?1 and acadyear=?2 and semester=?3 and subjectId=?4 and scoremanageType =?5 and classId in (?6)")
	void deleteBySubjectIdAndClassIdIn(String unitId, String acadyear,
			String semester, String subjectId, String scoreType,
			String[] classIds);
	
	@Query("from CompreScore where unitId= ?1 and scoremanageType = ?2 and examId in (?3)")
	List<CompreScore> findByExamIdIn(String unitId, String scoremanageType,
			String[] examIds);
	
	@Query("from CompreScore where acadyear= ?1 and semester = ?2 and unitId = ?3 and inputType = ?4 and scoremanageType = ?5 and examId in (?6)")
	List<CompreScore> findExamAllScores(String searchAcadyear,String searchSemester, String unitId, String inputType,String type,String[] examIds);

	@Modifying 
	@Query("Delete from CompreScore where unitId=?1 and examId=?2 and subjectId=?3 and scoremanageType =?4")
	void deleteByIdAndSubIdAndType(String unitId, String comInfoId,String subjectId, String type);

	@Modifying 
	@Query("Delete from CompreScore where unitId=?1 and examId=?2 and scoremanageType =?3 and studentId in (?4)")
	void deleteByIdsAndType(String unitId, String comInfoId, String type,String[] studentIds);

	@Query("from CompreScore where unitId=?1 and examId=?2 and scoremanageType=?3 and subjectId in (?4) order by subjectId,ranking")
	List<CompreScore> findByExamIdSubjectIdIn(String unitId, String examId,
			String type, String[] subjectIds, Pageable pageable);

	@Query("select count(id) from CompreScore where unitId=?1 and examId=?2 and scoremanageType=?3 and subjectId in (?4)")
	int getSizeByExamIdSubjectIdIn(String unitId, String examId, String type,
			String[] subjectIds);

	
	
	//分割线，以上为原有方法，方便后续删除
	@Query("from CompreScore where compreInfoId= ?1 and (scoremanageType <> '1' or (scoremanageType='1' and subject_id ='00000000000000000000000000000000'))")
	List<CompreScore> findByCompreInfoIdOnlyAll(String compreInfoId);

    @Query("from CompreScore where studentId = ?1 and scoremanageType = ?2")
	List<CompreScore> findByStudentIdAndType(String studentId, String type);

    @Modifying
    @Transactional
    @Query("Delete from CompreScore where unitId=?1 and scoremanageType =?2 and classId in (?3)")
    void deleteFirstList(String unitId, String scoreType, String[] classIds);

    @Query("from CompreScore where unitId=?1 and scoremanageType =?2 and studentId in (?3) ")
    List<CompreScore> getByStuIds(String unitId, String scoreType, String[] studentIds);

    @Query("from CompreScore where compreInfoId = ?1")
    List<CompreScore> findByCompreInfoId(String compreInfoId);

    @Query("from CompreScore where compreInfoId = ?1 and scoremanageType = ?2")
    List<CompreScore> findByCompreInfoIdAndScoremanageType(String compreInfoId, String scoremanageType);
	
	@Modifying 
	@Query("Delete from CompreScore where unitId=?1 and compreInfoId=?2 and subjectId in (?3) and scoremanageType =?4")
	void deleteByInfoIdAndSubIdAndType(String unitId, String infoId, String[] subjectIds, String type);

    @Query("from CompreScore where unitId = ?1 and scoremanageType = ?2")
    List<CompreScore> findByUnitIdAndType(String unitId, String type);

	List<CompreScore> findByUnitIdAndCompreInfoIdAndScoremanageType(String unitId, String infoId, String type);

	@Modifying 
	@Query("Delete from CompreScore where unitId=?1 and compreInfoId=?2 and scoremanageType =?3")
	void deleteByInfoIdAndType(String unitId, String infoId, String type);
	
	@Query("from CompreScore where scoremanageType = ?1 and studentId in (?2)")
	List<CompreScore> findByTypeAndStudentIds(String type, String[] studentIds);
}
