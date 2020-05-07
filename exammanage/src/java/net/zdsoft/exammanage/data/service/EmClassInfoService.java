package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmClassInfo;

import java.util.List;

public interface EmClassInfoService extends BaseService<EmClassInfo, String> {

    public List<EmClassInfo> findByExamIdAndSchoolId(String examId, String schoolId);

    public List<EmClassInfo> saveAllEntitys(EmClassInfo... emClassInfos);

    public void deleteByExamIdAndSchoolId(String examId, String schoolId);

    public List<EmClassInfo> findBySchoolIdAndExamIdIn(String[] examIds, String schoolId);

    public EmClassInfo findByExamIdAndSchoolIdAndClassId(String examId, String schoolId, String classId);

    /**
     * 根据班级id查到所有参与的考试
     *
     * @param classIds
     * @return
     */
    public List<String> findExamIdByClassIds(String[] classIds);

    public List<EmClassInfo> findByExamId(String examId);

    List<EmClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId, String... subjectInfoIds);

    void deleteByExamIds(String[] examIds);
}
