package net.zdsoft.stuwork.data.dto;

import java.util.ArrayList;
import java.util.List;

public class DyClassStatListDto {
	private String itemId;
	private String itemName;
	private String itemType;//1卫生2纪律3上课日志
	private String classId;
	private int day;
	private List<DyWeekCheckResultDto> dtos = new ArrayList<DyWeekCheckResultDto>();
	
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<DyWeekCheckResultDto> getDtos() {
		return dtos;
	}
	public void setDtos(List<DyWeekCheckResultDto> dtos) {
		this.dtos = dtos;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
}
