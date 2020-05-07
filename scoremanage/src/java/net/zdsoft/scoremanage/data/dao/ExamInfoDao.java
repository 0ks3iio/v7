package net.zdsoft.scoremanage.data.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.ExamInfo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExamInfoDao extends BaseJpaRepositoryDao<ExamInfo, String>{
	
	public static final String SQL_AFTER=" and isDeleted = 0 order by examCode desc";
	
	@Query("From ExamInfo where unitId = ?1"+SQL_AFTER)
	List<ExamInfo> findByUnitId(String unitId, Pageable page);
	
	@Query("From ExamInfo where unitId = ?1"+SQL_AFTER)
	List<ExamInfo> findByUnitId(String unitId);
	
	@Query("select count(*) from ExamInfo where unitId = ?1 and isDeleted = 0")
	Integer countByUnitId(String unitId);
	
//	@Query("From ExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3"+SQL_AFTER)
//	List<ExamInfo> findExamInfoList(String unitId, String acadyear, String semester, Pageable page);
	@Query("From ExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3 and isDeleted = 0 order by creationTime desc")
	List<ExamInfo> findExamInfoList(String unitId, String acadyear, String semester);
//	@Query("select count(*) from ExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3 and isDeleted = 0")
//	Integer countExamInfoList(String unitId, String acadyear, String semester);
	
	@Query("select examCode from ExamInfo order by examCode desc")
	List<String> findExamCodeMax();
	
	List<ExamInfo> findByIdIn(String... ids);
	
	@Modifying
	@Query("update ExamInfo set isDeleted=1 where id in (?1)")
	void updateIsDelete(String... ids);

	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester, Pageable page);
	
	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isgkExamType=?4 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester, String isgkExamType,Pageable page);
	
	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester);
	
	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isgkExamType=?4 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester,String isgkExamType);
	
	@Query("select count(t1.id) from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId = ?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isDeleted = 0 order by t1.examCode desc")
	Integer countExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester);

	@Query("select count(t1.id) from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId = ?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isgkExamType=?4 and t1.isDeleted = 0 order by t1.examCode desc")
	Integer countExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester,String isgkExamType);

//	@Query("From ExamInfo where examUeType!='3' and unitId = ?1 and acadyear = ?2 and semester = ?3"+SQL_AFTER)
//	List<ExamInfo> findExamInfoListNot(String unitId, String searchAcadyear, String searchSemester, Pageable pageable);
//	@Query("From ExamInfo where examUeType!='3' and unitId = ?1 and acadyear = ?2 and semester = ?3"+SQL_AFTER)
//	List<ExamInfo> findExamInfoListNot(String unitId, String searchAcadyear, String searchSemester);
//	@Query("select count(*) from ExamInfo where examUeType!='3' and unitId = ?1 and acadyear = ?2 and semester = ?3"+SQL_AFTER)
//	Integer countExamInfoListNot(String unitId, String searchAcadyear, String searchSemester);
	
	@Query("From ExamInfo where isDeleted=0 and id in (?1)")
	List<ExamInfo> findNotDeletedByIdIn(String... ids);
	
	@Query("From ExamInfo where unitId = ?1 and (acadyear || semester)<=  (?2 || ?3) and isDeleted = 0 order by (acadyear || semester) desc,examCode desc")
	List<ExamInfo> findExamInfoListRange(String unitId, String acadyear, String semester);
	@Query("From ExamInfo where examUeType!='3' and unitId = ?1 and (acadyear || semester)<=  (?2 || ?3) and isDeleted = 0 order by (acadyear || semester) desc,examCode desc")
	List<ExamInfo> findExamInfoListNotRange(String unitId, String acadyear, String semester);
	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and (t1.acadyear || t1.semester)<=  (?2 || ?3) and t1.isDeleted = 0 order by (t1.acadyear || t1.semester) desc,t1.examCode desc")
	List<ExamInfo> findExamInfoJoinListRange(String unitId, String acadyear, String semester);
	@Query("select distinct t1 from ExamInfo as t1,SubjectInfo t2 where t1.id=t2.examId and t1.unitId in (?1) and t1.acadyear = ?2 and t1.semester = ?3 and t2.rangeType = ?4 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoListAll(String[] unitIds, String acadyear, String semester, String gradeCode);
	@Query("select t1 from ExamInfo as t1,JoinexamschInfo as t2 where t1.id=t2.examInfoId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoJoinListAll(String unitId, String acadyear, String gradeCode);
	@Query("select distinct t1 from ExamInfo as t1,SubjectInfo t2 where t1.id=t2.examId and t1.id in (?1) and t2.rangeType = ?2 and t1.isDeleted = 0 order by t1.examCode desc")
	List<ExamInfo> findExamInfoListAll(String[] examIds, String gradeCode);
	@Query("from ExamInfo where unitId= ?1 and examType = ?2 and acadyear in (?3)")
	List<ExamInfo> findExamInfoByAcadyears(String unitId,String examType,String[] acadyears);
}
