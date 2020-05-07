package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class NewGkBuildingDto {

	private String buildingName;
	private String buildingId;
	private List<Integer> foorNumList;
	private String checkPlaceNames;
	
	
	public String getCheckPlaceNames() {
		return checkPlaceNames;
	}
	public void setCheckPlaceNames(String checkPlaceNames) {
		this.checkPlaceNames = checkPlaceNames;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}
	public String getBuildingId() {
		return buildingId;
	}
	public void setBuildingId(String buildingId) {
		this.buildingId = buildingId;
	}
	public List<Integer> getFoorNumList() {
		return foorNumList;
	}
	public void setFoorNumList(List<Integer> foorNumList) {
		this.foorNumList = foorNumList;
	}

	
}
