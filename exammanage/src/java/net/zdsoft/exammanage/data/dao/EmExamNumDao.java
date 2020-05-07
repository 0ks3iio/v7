package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmExamNum;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EmExamNumDao extends BaseJpaRepositoryDao<EmExamNum, String> {

    public List<EmExamNum> findBySchoolIdAndExamId(String schoolId, String examId);

    public void deleteByExamIdAndStudentIdIn(String examId, String[] studentIds);

    public void deleteBySchoolIdAndExamId(String schoolId, String examId);

    public List<EmExamNum> findByExamIdAndStudentIdIn(String examId,
                                                      String[] studentIds);
}
