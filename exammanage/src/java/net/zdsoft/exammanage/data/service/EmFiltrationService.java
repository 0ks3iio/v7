package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmFiltration;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EmFiltrationService extends BaseService<EmFiltration, String> {

    /**
     * @param examId
     * @param unitId
     * @param type   1：不排考
     * @return <学生id,学生id>
     */
    public Map<String, String> findByExamIdAndSchoolIdAndType(String examId,
                                                              String unitId, String type);

    public void deleteByExamIdAndStudentIdIn(String examId, String type, String[] studentIds);

    public List<EmFiltration> saveAllEntitys(EmFiltration... filtration);

    public void saveOrDel(List<EmFiltration> fList, String examId,
                          Set<String> studentIds, String type);

}

