package net.zdsoft.stuwork.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;

public interface DyStudentRewardSettingService extends BaseService<DyStudentRewardSetting, String>{


	public List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardGradeAndRewardLevel(
			String[] projectIds, String unitId);

	public List<DyStudentRewardSetting> findListByProjectIdsOrderByRewardPeriodAndRewardLevel(
			String[] projectIds, String unitId);

	public void saveSettingsOne(
			List<DyStudentRewardSetting> dyStudentRewardSettings, String unitId, String classesType);
	
	public void saveSettings(
			List<DyStudentRewardSetting> dyStudentRewardSettings, String unitId, String classesType);

//	public void addFestival(String rewardPeriod, String rewardClasses,
//			String unitId);

	public void addFestival(String rewardPeriod, String rewardClasses,
			String acadyear, String semester, String unitId);
	
	public void updateFestival(String[] projectIds,String rewardPeriod, String rewardClasses,
			String acadyear, String semester, String unitId);

	public void rewardGameSave(String rewardClasses, String projectName,
			String rewardGrade, String rewardLevel,String unitId,String projectId, String settingId);

	public List<DyStudentRewardSetting> findByUnitId(String unitId);

	public void deleteSetting(String settingId, String projectId);
	
	public void deleteByProIds(String unitId, String[] projectIds);

	public List<DyStudentRewardSetting> findInProjectId(String[] projectIds,
			Pagination page);

	public String doImport(List<String[]> datas,String unitId);


}
