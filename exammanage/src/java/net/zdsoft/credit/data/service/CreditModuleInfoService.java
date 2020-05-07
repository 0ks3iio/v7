package net.zdsoft.credit.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.dto.CredutDailyInfoDto;
import net.zdsoft.credit.data.entity.CreditExamSet;
import net.zdsoft.credit.data.entity.CreditModuleInfo;
import net.zdsoft.credit.data.entity.CreditSet;

public interface CreditModuleInfoService extends BaseService<CreditModuleInfo, String> {

    public List<CreditModuleInfo> findListByExamSetIds(String[] setIds,String scoreType);
    
	void saveModuleId(String unitId, String acadyear, String semester, String gradeId, String setId, String setExamId, String userId);

	void deleteBySetExamIds(String[] examSetIds);

	Map<String, CreditModuleInfo> findMapByStuIds(String acadyear, String semester, String subjectId, List<CreditExamSet> usualSetList,
			CreditExamSet moudleSet, CreditSet set, Set<String> stuIds);

	void saveDto(String unitId, CredutDailyInfoDto dto);

	int statNum(String unitId, String acadyear, String semester, String gradeId);
	
	public void importInfo(List<String> colls, List<String[]> datas, CreditSet set, String gradeId, String userId,
			String subjectId, String clsTypeId, Map<String, Student> stuMap, int successCount, int errorCount,
			List<String[]> errorDataList);

}
