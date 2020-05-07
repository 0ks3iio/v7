package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentSelectSubjectDao extends BaseJpaRepositoryDao<StudentSelectSubject, String>  {
    @Query("From StudentSelectSubject where acadyear=?1 and semester=?2 and schoolId=?3 and isDeleted=0")
    List<StudentSelectSubject> findBySchoolId(String acadyear, Integer semester, String schoolId);

    @Modifying
    @Query("update StudentSelectSubject set isDeleted=1 where acadyear=?1 and semester=?2 and studentId=?3")
    void deleteByStudentId(String acadyear, Integer semester, String studentId);

    @Modifying
    @Query("update StudentSelectSubject set isDeleted=1 where studentId in (?1)")
	void deleteByStudentIds(String... studentIds);

    @Modifying
    @Query("update StudentSelectSubject set isDeleted=1 where subjectId in (?1)")
	void deleteBySubjectIds(String... subjectIds);
    
    @Query("From StudentSelectSubject where acadyear=?1 and semester=?2 and gradeId=?3 and isDeleted=0")
    List<StudentSelectSubject> findByGrade(String acadyear, Integer semester, String gradeId);
}
