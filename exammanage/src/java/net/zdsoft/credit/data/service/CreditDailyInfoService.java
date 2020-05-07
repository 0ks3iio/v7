package net.zdsoft.credit.data.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.credit.data.dto.CredutDailyInfoDto;
import net.zdsoft.credit.data.entity.CreditDailyInfo;
import net.zdsoft.credit.data.entity.CreditSet;

public interface CreditDailyInfoService extends BaseService<CreditDailyInfo, String> {
    List<CreditDailyInfo> findListBySubDailySetIds(String[] setIds);
	Map<String, CreditDailyInfo> findMapByStuIds(String acadyear, String semester,String subjectId, CreditSet set, Set<String> stuIds);

	void saveDto(String unitId, CredutDailyInfoDto dto);

	int statNum(String acadyear, String semester, String unitId, String gradeId);
	//导入日常表现
	void importInfo(List<String> titleList, List<String[]> datas, CreditSet set, String gradeId, String userId,
			String subjectId, String clsTypeId, Map<String,Student> stuMap, int successCount, int errorCount, List<String[]> errorDataList);

	List<CreditDailyInfo> findListByNullDailyId(String acadyear, String unitId, String semester,String gradeId);

	List<CreditDailyInfo> findListByNullDailyIdByStuId(String acadyear, String unitId, String semester,String studentId);

	List<CreditDailyInfo> findListByNotNullDailyId(String acadyear, String semester,String studentId);

	List<CreditDailyInfo> findListByClassIdAndNotNullDailyId(String acadyear, String semester,String classId,String subjectId);

}
