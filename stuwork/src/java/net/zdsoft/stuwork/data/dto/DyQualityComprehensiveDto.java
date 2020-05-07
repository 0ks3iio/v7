package net.zdsoft.stuwork.data.dto;

import java.util.List;

public class DyQualityComprehensiveDto {
	
	private String[] acadyearArray;
	
	private List<String[]> valueList;
	
	private Float countNum;

	public String[] getAcadyearArray() {
		return acadyearArray;
	}

	public void setAcadyearArray(String[] acadyearArray) {
		this.acadyearArray = acadyearArray;
	}

	public List<String[]> getValueList() {
		return valueList;
	}

	public void setValueList(List<String[]> valueList) {
		this.valueList = valueList;
	}

	public Float getCountNum() {
		return countNum;
	}

	public void setCountNum(Float countNum) {
		this.countNum = countNum;
	}

	
}
