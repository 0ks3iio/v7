package net.zdsoft.exammanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.exammanage.data.entity.EmOption;
import net.zdsoft.exammanage.data.entity.EmOptionSchool;

import java.util.List;
import java.util.Set;

public interface EmOptionService extends BaseService<EmOption, String> {

    public List<EmOption> findByExamIdAndExamRegionId(String examId, String examRegionId);

    public List<EmOption> findByExamIdWithMaster(String examId);

    public List<EmOption> findByExamIdAndSchools(String examId, String... schools);

    public void saveEmOptionsAndEmOptionSchools(List<EmOption> emOptions, List<EmOptionSchool> emOptionSchools, String examId, String examRegionId, int optionSize);

    public void deleteByExamIdAndExamRegionId(String examId, String examRegionId);

    public List<EmOption> saveAllEntitys(EmOption... emOptions);

    public void deleteByOptionIdAndExamId(String optionId, String examId);

    public Set<String> getExamRegionId(String examId);

    public List<EmOption> findByExamIdAndExamRegionIdIn(String examId, String examRegionId, String[] examRegionIds);
}
