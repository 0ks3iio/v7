package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmLimitDetail;

import java.util.List;

public interface EmLimitDetailService extends BaseService<EmLimitDetail, String> {

    public List<EmLimitDetail> findByLimitIdIn(String[] limitIds);

    public void deleteAllByIds(String... ids);

    public List<EmLimitDetail> saveAllEntitys(EmLimitDetail... emLimitDetailList);

    public void deleteByLimitId(String[] limitId);

    public List<EmLimitDetail> findBySubjectTeacher(String examId,
                                                    String teacherId, String subjectId);

}
