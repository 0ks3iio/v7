package net.zdsoft.newgkelective.data.dto;

public class TimeInfDto {
	private Integer day_of_week;
	private String period_interval;
	private Integer period;
	private String time_type;
	public Integer getDay_of_week() {
		return day_of_week;
	}
	public void setDay_of_week(Integer day_of_week) {
		this.day_of_week = day_of_week;
	}
	public String getPeriod_interval() {
		return period_interval;
	}
	public void setPeriod_interval(String period_interval) {
		this.period_interval = period_interval;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public String getTime_type() {
		return time_type;
	}
	public void setTime_type(String time_type) {
		this.time_type = time_type;
	}
}
