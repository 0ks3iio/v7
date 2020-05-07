package net.zdsoft.basedata.dto;

public class TimePlaceDto {
	
	private String placeId;
	private Integer dayOfWeek; // 星期
	private String periodInterval; // 时间段
	private Integer period; // 节次
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	public Integer getDayOfWeek() {
		return dayOfWeek;
	}
	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}
	public String getPeriodInterval() {
		return periodInterval;
	}
	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}

	
}
