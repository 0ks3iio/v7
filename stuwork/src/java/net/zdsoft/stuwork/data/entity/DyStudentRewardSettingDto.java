package net.zdsoft.stuwork.data.entity;

import java.util.ArrayList;
import java.util.List;

public class DyStudentRewardSettingDto {
	private static final long serialVersionUID = 8698717021761801559L;
	
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
