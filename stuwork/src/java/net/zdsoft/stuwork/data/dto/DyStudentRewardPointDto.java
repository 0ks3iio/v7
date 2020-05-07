package net.zdsoft.stuwork.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.stuwork.data.entity.DyStudentRewardPoint;
import net.zdsoft.stuwork.data.entity.DyStudentRewardProject;
import net.zdsoft.stuwork.data.entity.DyStudentRewardSetting;

public class DyStudentRewardPointDto {
	
	private DyStudentRewardProject dyStudentRewardProject;
	
	private DyStudentRewardSetting dyStudentRewardSetting;
	
	private List<DyStudentRewardPoint> dyStudentRewardPoints = new ArrayList<DyStudentRewardPoint>();
	
	public DyStudentRewardProject getDyStudentRewardProject() {
		return dyStudentRewardProject;
	}

	public void setDyStudentRewardProject(
			DyStudentRewardProject dyStudentRewardProject) {
		this.dyStudentRewardProject = dyStudentRewardProject;
	}

	public DyStudentRewardSetting getDyStudentRewardSetting() {
		return dyStudentRewardSetting;
	}

	public void setDyStudentRewardSetting(
			DyStudentRewardSetting dyStudentRewardSetting) {
		this.dyStudentRewardSetting = dyStudentRewardSetting;
	}

	public List<DyStudentRewardPoint> getDyStudentRewardPoints() {
		return dyStudentRewardPoints;
	}

	public void setDyStudentRewardPoints(
			List<DyStudentRewardPoint> dyStudentRewardPoints) {
		this.dyStudentRewardPoints = dyStudentRewardPoints;
	}

	
}
