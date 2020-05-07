package net.zdsoft.stuwork.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;

public class DyStudentRewardSettingDto {
	
	private DyStudentRewardProject dyStudentRewardProject;
	
	private List<DyStudentRewardSetting> dyStudentRewardSettings = new ArrayList<DyStudentRewardSetting>();
	
	
	
	
	public DyStudentRewardProject getDyStudentRewardProject() {
		return dyStudentRewardProject;
	}

	public void setDyStudentRewardProject(
			DyStudentRewardProject dyStudentRewardProject) {
		this.dyStudentRewardProject = dyStudentRewardProject;
	}

	public List<DyStudentRewardSetting> getDyStudentRewardSettings() {
		return dyStudentRewardSettings;
	}

	public void setDyStudentRewardSettings(
			List<DyStudentRewardSetting> dyStudentRewardSettings) {
		this.dyStudentRewardSettings = dyStudentRewardSettings;
	}
	
}
