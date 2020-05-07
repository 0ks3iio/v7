package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmEnrollStudent;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface EmEnrollStudentDao extends BaseJpaRepositoryDao<EmEnrollStudent, String>, EmEnrollStudentJdbcDao {

    public List<EmEnrollStudent> findByExamId(String examId);

    @Modifying
    @Query("update from EmEnrollStudent set hasPass = ?1 Where examId = ?2 and studentId in (?3)")
    public void updateforHaspass(String hasPass, String examId, String[] studentIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolId(String examId, String schoolId);

    @Query("From EmEnrollStudent Where studentId in (?1)")
    public List<EmEnrollStudent> findByStudentIdIn(String[] studentIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolIdIn(String examId, String[] schoolIds);

    public List<EmEnrollStudent> findByExamIdAndHasPass(String examId, String hasPass);


    @Query("From EmEnrollStudent Where examId in (?1)")
    public List<EmEnrollStudent> findByExamIdIn(String[] examIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolIdAndClassIdIn(String examId, String schoolId, String[] classIds);

    public List<EmEnrollStudent> findByExamIdAndSchoolIdAndStudentIdIn(String examId, String schoolId, String[] studentIds);

    void deleteByExamIdAndUnitId(String examId, String unitId);

    public List<EmEnrollStudent> findByStudentIdAndExamIdIn(String studentId, String[] examIds);

    @Query("From EmEnrollStudent Where studentId = ?1 and hasPass = ?2")
    public List<EmEnrollStudent> findByStuIdAndState(String stuId, String state);

    @Query("select distinct classId From EmEnrollStudent Where examId = ?1 and schoolId = ?2 and hasPass = '1'")
    public Set<String> findClsIdByExamIdAndSchoolId(String examId, String schoolId);
}
