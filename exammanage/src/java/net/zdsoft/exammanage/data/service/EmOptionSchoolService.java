package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.dto.EmOptionSchoolDto;
import net.zdsoft.exammanage.data.entity.EmOptionSchool;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

public interface EmOptionSchoolService extends BaseService<EmOptionSchool, String> {

    public List<EmOptionSchoolDto> findByDtos(String unitId, String examId, Pagination page);

    public void updateOptionStuNum(List<EmOptionSchool> ens);

    public List<EmOptionSchool> findByOptionIdWithMaster(String examId, String... optionIds);

    public void deleteByOptionIdsAndExamId(String examId, String... optionIds);

    public List<EmOptionSchool> saveAllEntitys(EmOptionSchool... emOptionSchools);

    public List<EmOptionSchool> findByExamId(String examId);
}
