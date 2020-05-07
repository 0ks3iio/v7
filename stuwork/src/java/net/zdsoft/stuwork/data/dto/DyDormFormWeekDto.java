package net.zdsoft.stuwork.data.dto;

public class DyDormFormWeekDto {

	private String itemType;//1表扬2批评3其他
	private int day;
	private String data;
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
	
}
