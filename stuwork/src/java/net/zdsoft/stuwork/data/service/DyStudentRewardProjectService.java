package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.dto.DyStudentRewardSettingDto;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;

public interface DyStudentRewardProjectService extends BaseService<DyStudentRewardProject, String>{

	List<DyStudentRewardSettingDto> findListByClassType(String classesType,
			String unitId, boolean withDefault, String rewardPeriod);

	List<DyStudentRewardProject> findListByRewardPeriodAndRewardClasses(
			String rewardPeriod, String rewardClasses, String unitId);

	DyStudentRewardProject findByClassesAndName(String rewardClasses,
			String projectName, String unitId);

	List<DyStudentRewardProject> findByClasses(String rewardClasses, String classType, String unitId);

	List<DyStudentRewardProject> findByUnitId(String unitId);

	List<DyStudentRewardSettingDto> findListByFestival(String classesType,
			String unitId, Pagination page);

	List<DyStudentRewardProject> findByClassesType(String classesType, String unitId);
	
	void deletByIds(String[] ids);
}
