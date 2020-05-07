package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmStudentGroup;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmStudentGroupService extends BaseService<EmStudentGroup, String> {

    List<EmStudentGroup> findByGroupIdAndSchoolId(String groupId, String schoolId);

    List<EmStudentGroup> findByGroupIdAndExamIdAndSchoolId(String groupId, String examId, String schoolId);

    Map<String, Set<String>> findGroupMap(String unitId, String examId, String[] groupIds);

    void deleteByExamIdAndSchoolId(String examId, String schoolId);

}
