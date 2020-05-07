package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmClassLock;

import java.util.List;

public interface EmClassLockService extends BaseService<EmClassLock, String> {

    List<EmClassLock> findBySchoolIdAndExamIdAndSubjectId(String schoolId, String examId, String subjectId);

    EmClassLock findBySchoolIdAndExamIdAndSubjectIdAndClassId(String schoolId, String examId, String subjectId, String classId);

    void deleteBy(String examId, String subjectId);

    void deleteByExamIdIn(String[] examIds);
}
