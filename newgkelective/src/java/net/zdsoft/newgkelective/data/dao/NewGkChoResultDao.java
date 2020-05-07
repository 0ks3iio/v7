package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkChoResult;

public interface NewGkChoResultDao extends BaseJpaRepositoryDao<NewGkChoResult, String>{
	
	static final String SQL_AFTER=" and exists(select 1 from Student s where s.id=t1.studentId and s.isDeleted=0)";

	 @Query("from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType in (?2) and t1.choiceId=?3"+SQL_AFTER)
	List<NewGkChoResult> findByUnitIdAndKindTypeInAndChoiceId(String unitId,String[] kindTypes,String choiceId);
	
	List<NewGkChoResult> findByUnitIdAndKindTypeInAndChoiceIdAndStudentId(String unitId,String[] kindType,String choiceId,String studentId);

	@Query("select t1.choiceId,COUNT(DISTINCT t1.studentId) from NewGkChoResult as t1, NewGkChoice t2 where t2.gradeId =?1 and t2.isDeleted=0 and t2.id =t1.choiceId and t1.kindType=?2"+SQL_AFTER+" group by t1.choiceId")
    public List<Object[]> countByGradeIdAndKindType(String gradeId,String kindType);

    @Query("select t1 from NewGkChoResult as t1, NewGkChoice t2 where t2.gradeId =?1 and t2.isDeleted=0 and t2.id =t1.choiceId and t1.kindType=?2"+SQL_AFTER+" order by t1.choiceId ")
	List<NewGkChoResult> findByGradeIdAndKindType(String gradeId,String kindType);
    
    @Query("select t1 from NewGkChoResult as t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId in (?3)"+SQL_AFTER)
    List<NewGkChoResult> findByGradeIdAndKindType(String unitId,String kindType ,String[] choiceIds );

	List<NewGkChoResult> findByUnitIdAndKindTypeAndChoiceIdAndStudentIdIn(String unitId,String kindType,String choiceId,
			String[] studentId);

	@Query("select count(distinct t1.studentId) from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId=?3"+SQL_AFTER)
	int findCountByKindTypeAndChoiceId(String unitId,String kindType,String choiceId);
	
	@Query("select count(t1.id), t1.choiceId,t1.subjectId from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId in (?3)"+SQL_AFTER+" group by t1.choiceId,t1.subjectId ")
	List<Object[]> findCountByChoiceIdAndKindType(String unitId,String kindType,String[] choiceIds);
	
	@Query("select count(t1.id), t1.choiceId,t1.subjectId from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId in (?3) and t1.studentId in (?4)"+SQL_AFTER+" group by t1.choiceId,t1.subjectId ")
	List<Object[]> findCountByChoiceIdAndKindTypeAndStudentIds(String unitId,String kindType,String[] choiceIds, String[] studentIds);

	@Query("select t1.studentId from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId=?3 and t1.subjectId=?4"+SQL_AFTER)
	List<String> findStudentBySubjectAndKindType(String unitId ,String kindType, String choiceId, String subjectId);

	@Query("from NewGkChoResult t1 where t1.unitId=?1 and t1.kindType=?2 and t1.choiceId=?3  and t1.subjectId in (?4)"+SQL_AFTER)
	List<NewGkChoResult> findStuIdListByChoiceIdAndSubjectId(String unitId, String kindType,String choiceId,String[] subjectId);

    // Basedata Sync Method
    void deleteByStudentIdIn(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);

	List<NewGkChoResult> findByUnitIdAndStudentIdAndChoiceIdIn(String unitId, String studentId, String[] choiceIds);
}
