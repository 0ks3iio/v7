package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmExamNum;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmExamNumService extends BaseService<EmExamNum, String> {

    public Map<String, String> findBySchoolIdAndExamId(String schoolId, String examId);

    /**
     * 或者以考号为key的map value是studentId
     *
     * @param schoolId
     * @param examId
     * @return
     */
    public Map<String, String> findNumStuIdMap(String schoolId, String examId);

    public void addOrDel(List<EmExamNum> insertList, String examId, Set<String> delStudentIds);

    public List<EmExamNum> saveAllEntitys(EmExamNum... exammanageExamNum);

    public void insertAllAndDeleteAll(List<EmExamNum> insertList, String examId,
                                      String unitId);

    public void deleteBy(String unitId, String examId);

    public List<EmExamNum> findunitIdAndExamId(String unitId, String examId);

    /**
     * @param examId
     * @param studentIds
     * @return key:studentId
     */
    public Map<String, String> findByExamIdAndStudentIdIn(String examId, String[] studentIds);
}
