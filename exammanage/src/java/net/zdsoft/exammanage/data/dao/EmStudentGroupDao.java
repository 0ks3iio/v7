package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmStudentGroup;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface EmStudentGroupDao extends BaseJpaRepositoryDao<EmStudentGroup, String> {

    List<EmStudentGroup> findByGroupIdAndSchoolId(String groupId, String unitId);

    List<EmStudentGroup> findByGroupIdAndExamIdAndSchoolId(String groupId, String examId, String unitId);

    List<EmStudentGroup> findListBySchoolIdAndExamIdAndGroupIdIn(String schoolId, String examId, String[] groupIds);

    void deleteByExamIdAndSchoolId(String examId, String schoolId);
}
