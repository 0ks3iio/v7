package net.zdsoft.eclasscard.data.dto;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccTimingSet;

public class TimingSetDto {

	private String unitId;
	private String openTime;
	private String closeTime;
	private String[] weekChoose;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getOpenTime() {
		return openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String[] getWeekChoose() {
		return weekChoose;
	}

	public void setWeekChoose(String[] weekChoose) {
		this.weekChoose = weekChoose;
	}
}
