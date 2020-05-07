package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmAimsStudent;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmAimsStudentDao extends BaseJpaRepositoryDao<EmAimsStudent, String> {

    @Query("From EmAimsStudent where studentId = ?1 and aimsId in (?2)")
    List<EmAimsStudent> findListByStuIdAndAimsIds(String stuId, String[] aimsIds);
    
    @Query("From EmAimsStudent where aimsId = ?1 and schoolId=?2")
    List<EmAimsStudent> findListByAimsIdAndSchoolId(String aimsId, String schoolId);
}
