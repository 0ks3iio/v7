package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.entity.School;

public class SchoolDto {
	
	private School school;
	
	private String regionName;

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
	
}
