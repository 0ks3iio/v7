package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisSetSubject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/22 16:22
 */
public interface DiathesisSetSubjectDao extends BaseJpaRepositoryDao<DiathesisSetSubject,String> {

    @Query("from DiathesisSetSubject where gradeId=?1 and gradeCode=?2 and semester=?3 and subjectType=?4")
    List<DiathesisSetSubject> findByGradeIdAndGradeCodeAndSemesterAndType(String gradeId, String gradeCode, Integer semester, String type);

    /*@Query(value = "select count(*) from NEWDIATHESIS_SET_SUBJECT where grade_id =?1",nativeQuery = true)
    Integer countByGradeIdAndGradeCodeAndSemesterAndType(String gradeId, String gradeCode, Integer semester, String subjectType);
*/
    @Modifying
    @Query("delete from DiathesisSetSubject where gradeId=?1 and gradeCode=?2 and  semester =?3 and subjectType=?4")
    void deleteByGradeIdAndGradeCodeAndSemesterAndType(String gradeId, String gradeCode, Integer semester, String subjectType);

    @Query("from DiathesisSetSubject where gradeId=?1 and subjectType=?2")
    List<DiathesisSetSubject> findByGradeIdAndType(String gradeId,String type);
}
