package net.zdsoft.gkelective.data.dto;

import java.util.List;

import net.zdsoft.basedata.entity.Student;

public class GkGroupClassDto {
	
	private String groupClassId;
	private String groupName;
	private Integer number;//人数
	private List<Student> stuList;
	public String getGroupClassId() {
		return groupClassId;
	}
	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public List<Student> getStuList() {
		return stuList;
	}
	public void setStuList(List<Student> stuList) {
		this.stuList = stuList;
	}

}
