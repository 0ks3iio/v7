package net.zdsoft.newgkelective.data.dto;

import java.util.List;

public class CourseFeatureContainer {
	private List<NewGKCourseFeatureDto> dtoList;
	
	//如果政治A与历史不连排 同样需要一条历史与政治A不连排 页面为一条数据 后台保存2条
	private String noContinueSubjectIds;

	public List<NewGKCourseFeatureDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<NewGKCourseFeatureDto> dtoList) {
		this.dtoList = dtoList;
	}

	public String getNoContinueSubjectIds() {
		return noContinueSubjectIds;
	}

	public void setNoContinueSubjectIds(String noContinueSubjectIds) {
		this.noContinueSubjectIds = noContinueSubjectIds;
	}

	
	
	
}
