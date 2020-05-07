package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmEnrollStuCount;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface EmEnrollStuCountService extends BaseService<EmEnrollStuCount, String> {

    public List<EmEnrollStuCount> findByExamId(String examId, Pagination page);

    public List<EmEnrollStuCount> findByExamIdAndSchoolIdIn(String examId, String[] schoolIds);

}
