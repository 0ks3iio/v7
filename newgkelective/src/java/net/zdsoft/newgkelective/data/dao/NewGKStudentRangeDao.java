package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGKStudentRange;

public interface NewGKStudentRangeDao  extends BaseJpaRepositoryDao<NewGKStudentRange, String>{
	
	List<NewGKStudentRange> findByUnitIdAndDivideIdAndSubjectId(String unitId, String divideId, String subjectId);
	
	List<NewGKStudentRange> findByUnitIdAndDivideId(String unitId, String divideId);
	
	@Query(value="select range, count(studentId) From NewGKStudentRange where unitId=?1 and divideId=?2 and subjectId=?3 and subjectType=?4 group by range")
	List<Object[]> findStuRangeCount(String unitId, String divideId, String subjectId, String subjectType);

    // Basedata Sync Method
    void deleteByStudentIdIn(String... stuids);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subids);

    @Query(value = "select distinct(range), subjectId from NewGKStudentRange where unitId=?1 and divideId=?2 and subjectType=?3")
    List<Object[]> findSubjectRanges(String unitId, String divideId, String subjectType);

    @Query("from NewGKStudentRange where unitId=?1 and divideId=?2 and studentId=?3")
	List<NewGKStudentRange> findByDivideIdAndStudentId(String unitId, String divideId, String studentId);
}
