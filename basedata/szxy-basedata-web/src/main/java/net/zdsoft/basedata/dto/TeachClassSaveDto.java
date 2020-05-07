package net.zdsoft.basedata.dto;

import java.util.List;

import net.zdsoft.basedata.entity.TeachClass;

public class TeachClassSaveDto {
	
	private TeachClass teachClass;
	
	private String relaOpen;//是否关联走班课程
	//上课时间 场地
	private List<TimePlaceDto> timePlaceDtoList;
	
	public TeachClass getTeachClass() {
		return teachClass;
	}
	public void setTeachClass(TeachClass teachClass) {
		this.teachClass = teachClass;
	}
	public List<TimePlaceDto> getTimePlaceDtoList() {
		return timePlaceDtoList;
	}
	public void setTimePlaceDtoList(List<TimePlaceDto> timePlaceDtoList) {
		this.timePlaceDtoList = timePlaceDtoList;
	}
	public String getRelaOpen() {
		return relaOpen;
	}
	public void setRelaOpen(String relaOpen) {
		this.relaOpen = relaOpen;
	}

	
}
