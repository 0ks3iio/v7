package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.ExamInfoDto;
import net.zdsoft.exammanage.data.dto.ScoreInfoDto;
import net.zdsoft.exammanage.data.dto.SubjectInfoDto;
import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.exammanage.data.entity.EmSubjectInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmSubjectInfoService extends BaseService<EmSubjectInfo, String> {

    public List<EmSubjectInfo> findByExamId(String examId);

    public List<EmSubjectInfo> findByUnitIdAndExamId(String UnitId, String... examIds);

    public List<EmSubjectInfo> saveAllEntitys(EmSubjectInfo... emSubjectInfo);

    public void deleteByIdIn(String... id);

    /**
     * @param examId
     * @param SchoolId       班级对应的单位
     * @param emSubjectInfos
     * @param classIds
     */
    public void saveSubjectClass(EmExamInfo examInfo, String schoolId,
                                 List<EmSubjectInfo> emSubjectInfos, String[] classIds);

    public List<EmSubjectInfo> findByExamIdAndSubjectId(String examId,
                                                        String subjectId);

    public void updateIsLock(String examId, String isLock);

    public Map<String, Set<String>> findTeacher(String examId, String subjectId, String unitId, Set<String> classIds, Set<String> teachClassIds);

    public List<String> findSubIdByExamIds(Set<String> examIds);

    public List<EmSubjectInfo> findByExamIds(Set<String> examIds);

    public void deleteByExamIdIn(String[] examIds);

    public void synch(ExamInfoDto examDto, List<ScoreInfoDto> scoreDtoList, List<SubjectInfoDto> subjectDtoList);

}
